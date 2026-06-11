from __future__ import annotations

import os
from pathlib import Path
from typing import Any


DEFAULT_CONFIG_PATH = Path(__file__).resolve().parent / "config.yml"
DEFAULT_AWS_CREDS_PATH = Path(__file__).resolve().parent / ".awscreds"


class KafkaAutomationError(RuntimeError):
    """Raised when Kafka automation setup or IO fails."""


def load_config(
    config_path: str | os.PathLike[str] | None = None,
    *,
    env: str | None = None,
    user: str | None = None,
) -> dict[str, Any]:
    """Load YAML config and expand env/user based references."""

    try:
        import yaml
    except ImportError as exc:
        raise KafkaAutomationError("PyYAML is required. Install with `uv sync`.") from exc

    path = Path(config_path) if config_path else DEFAULT_CONFIG_PATH
    with path.open("r", encoding="utf-8") as handle:
        config = yaml.safe_load(handle) or {}

    config["env"] = env or os.getenv("KAFKA_AUTOMATION_ENV") or config.get("env", "dev")
    config["user"] = user or os.getenv("KAFKA_AUTOMATION_USER") or config.get("user") or os.getenv(
        "USER", "local"
    )
    _apply_environment_config(config)

    context = _template_context(config)
    if isinstance(config.get("msk", {}).get("stage"), str):
        config["msk"]["stage"] = config["msk"]["stage"].format(**context)
    config["topics"] = _expand_mapping(config.get("topics", {}), context)
    config["clients"] = _expand_mapping(config.get("clients", {}), context)

    return config


def get_secret_value(config: dict[str, Any], secret_id: str | None = None) -> str:
    """Read a secret value from AWS Secrets Manager using the configured profile."""

    session = _aws_session(config)
    client = session.client("secretsmanager", region_name=config["aws"]["region"])
    response = client.get_secret_value(SecretId=secret_id or _broker_secret_id(config))
    value = response.get("SecretString")
    if not value:
        raise KafkaAutomationError("Secrets Manager returned an empty SecretString.")
    return value


def get_parameter_value(config: dict[str, Any], parameter_name: str | None = None) -> str:
    """Read a parameter value from AWS Systems Manager Parameter Store."""

    session = _aws_session(config)
    client = session.client("ssm", region_name=config["aws"]["region"])
    response = client.get_parameter(Name=parameter_name or _broker_parameter_name(config), WithDecryption=True)
    value = response.get("Parameter", {}).get("Value")
    if not value:
        raise KafkaAutomationError("SSM Parameter Store returned an empty parameter value.")
    return value


def get_bootstrap_servers(config: dict[str, Any]) -> str:
    """Resolve MSK bootstrap brokers from the configured AWS source."""

    source = config.get("msk", {}).get("parameter_source", "secretsmanager").lower()
    if source == "secretsmanager":
        return get_secret_value(config)
    if source in {"ssm", "parameterstore", "parameter_store"}:
        return get_parameter_value(config)
    raise KafkaAutomationError(f"Unsupported MSK parameter_source: {source}")


def _aws_session(config: dict[str, Any]):
    try:
        import boto3
    except ImportError as exc:
        raise KafkaAutomationError("boto3 is required. Install with `uv sync`.") from exc

    aws_config = config.get("aws", {})
    credentials_path = Path(aws_config.get("credentials_file") or DEFAULT_AWS_CREDS_PATH)
    if credentials_path.exists():
        credentials = _load_aws_credentials(credentials_path)
        return boto3.Session(
            aws_access_key_id=credentials["aws_access_key_id"],
            aws_secret_access_key=credentials["aws_secret_access_key"],
            aws_session_token=credentials.get("aws_session_token"),
            region_name=aws_config.get("region"),
        )

    profile = aws_config.get("profile")
    return boto3.Session(profile_name=profile) if profile else boto3.Session()


def _load_aws_credentials(path: Path) -> dict[str, str]:
    credentials: dict[str, str] = {}
    for line_number, raw_line in enumerate(path.read_text(encoding="utf-8").splitlines(), start=1):
        line = raw_line.strip()
        if not line or line.startswith("#"):
            continue
        if "=" not in line:
            raise KafkaAutomationError(f"Invalid .awscreds line {line_number}: expected key=value")
        key, value = line.split("=", 1)
        credentials[key.strip()] = value.strip()

    required_keys = {"aws_access_key_id", "aws_secret_access_key"}
    missing_keys = sorted(key for key in required_keys if not credentials.get(key))
    if missing_keys:
        raise KafkaAutomationError(f"Missing required .awscreds value(s): {', '.join(missing_keys)}")

    return credentials


def _broker_secret_id(config: dict[str, Any]) -> str:
    template = config["msk"].get("broker_secret_id_template", "{project}/{stage}/brokerString")
    return template.format(**_template_context(config))


def _broker_parameter_name(config: dict[str, Any]) -> str:
    template = config["msk"].get("broker_parameter_name_template", "/{project}/{stage}/brokerString")
    return template.format(**_template_context(config))


def _template_context(config: dict[str, Any]) -> dict[str, str]:
    msk_config = config.get("msk", {})
    return {
        "env": str(config.get("env", "dev")),
        "topic_suffix": str(config.get("topic_suffix", config.get("env", "dev"))),
        "msk_stage": str(config.get("msk_stage", msk_config.get("stage", "master"))),
        "user": _identifier_part(str(config.get("user", "local"))),
        "project": str(msk_config.get("project", "bigmac")),
        "stage": str(msk_config.get("stage", "master")),
    }


def _apply_environment_config(config: dict[str, Any]) -> None:
    env = str(config.get("env", "dev"))
    environments = config.get("environments", {})
    if not environments:
        return

    environment = environments.get(env)
    if not environment:
        available = ", ".join(sorted(environments))
        raise KafkaAutomationError(f"Unsupported env '{env}'. Expected one of: {available}")

    for key, value in environment.items():
        config[key] = value


def _expand_mapping(values: dict[str, Any], context: dict[str, str]) -> dict[str, Any]:
    expanded: dict[str, Any] = {}
    for key, value in values.items():
        expanded[key] = value.format(**context) if isinstance(value, str) else value
    return expanded


def _identifier_part(value: str) -> str:
    return "".join(character if character.isalnum() or character in {"-", "_"} else "-" for character in value)
