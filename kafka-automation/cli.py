from __future__ import annotations

import argparse
import json
import sys
import time
from datetime import UTC, datetime
from pathlib import Path
from typing import Any

from kafka_utils import KafkaMessage, iter_messages, publish_message, purge_topic
from sqlite_utils import (
    create_messages_table,
    create_published_messages_table,
    initialize_db,
    insert_row,
    message_to_row,
)
from utils import DEFAULT_AWS_CREDS_PATH, DEFAULT_CONFIG_PATH, get_bootstrap_servers, load_config


def main() -> None:
    parser = argparse.ArgumentParser(description="OneMAC MSK automation utilities")
    parser.add_argument("--config", help="Path to config.yml")
    parser.add_argument("--env", choices=["dev", "qa"], help="Environment, dev or qa")
    parser.add_argument("--user", help="Identifier suffix for Kafka clients/groups")

    subparsers = parser.add_subparsers(dest="command", required=True)

    publish_parser = subparsers.add_parser("publish", help="Publish a JSON payload")
    publish_parser.add_argument("payload_file", help="JSON payload file")
    publish_parser.add_argument("--topic", help="Kafka topic or configured topic key")
    publish_parser.add_argument("--key", help="Message key. Defaults to payload.id")
    publish_parser.add_argument(
        "--header",
        action="append",
        default=[],
        metavar="NAME=VALUE",
        help="Message header. Repeatable. Defaults to origin=mako when omitted.",
    )
    publish_parser.add_argument(
        "--persist",
        choices=["sqlite"],
        help="Persist published messages to the configured backend",
    )

    scenario_parser = subparsers.add_parser("scenario", help="Publish all messages for a scenario")
    scenario_parser.add_argument("--scenario", required=True, help="Scenario id, for example s001")

    receive_parser = subparsers.add_parser("receive", help="Receive Kafka messages")
    receive_parser.add_argument("--topic", help="Kafka topic or configured topic key")
    receive_parser.add_argument("--count", type=int, help="Maximum number of messages to read")
    receive_parser.add_argument("--offset", type=int, help="Offset, or -1 for consumer group")
    receive_parser.add_argument("--partition", type=int)
    receive_parser.add_argument("--timeout", type=float, help="Receive timeout in seconds")
    receive_parser.add_argument("--consumer-group-id", help="Override configured consumer group id")
    receive_parser.add_argument(
        "--persist",
        choices=["sqlite"],
        help="Persist received messages to the configured backend",
    )
    receive_parser.add_argument(
        "--commit",
        action=argparse.BooleanOptionalAction,
        default=None,
        help="Commit received offset",
    )

    subparsers.add_parser("purge", help="Delete all messages from configured inbound or outbound topic")

    args = parser.parse_args()
    _status("Loading config")
    config = load_config(args.config, env=args.env, user=args.user)

    if args.command == "publish":
        bootstrap_servers = _resolve_bootstrap_servers(config)
        publish_config = config.get("cli", {}).get("publish", {})
        producer_config = config.get("producer", {})
        msk_config = config.get("msk", {})
        payload = _load_json(args.payload_file)
        headers = _parse_headers(args.header) if args.header else publish_config.get("headers")
        topic = _resolve_topic(config, args.topic or publish_config.get("topic") or "outbound")
        _status(f"Publishing to {topic}")
        result = publish_message(
            payload,
            topic=topic,
            bootstrap_servers=bootstrap_servers,
            client_id=config["clients"]["producer_client_id"],
            security_protocol=msk_config["security_protocol"],
            ssl_endpoint_identification_algorithm=msk_config.get("ssl_endpoint_identification_algorithm"),
            delivery_timeout_ms=int(producer_config["delivery_timeout_ms"]),
            flush_timeout_seconds=float(publish_config.get("flush_timeout_seconds", 30)),
            key=args.key,
            headers=headers,
            producer_extra_config=producer_config.get("extra_config"),
        )
        sqlite_config = config.get("sqlite", {})
        if _should_persist_sqlite(args.persist, sqlite_config):
            db_connection = initialize_db(
                _resolve_run_folder(config, args.config),
                db_name=sqlite_config.get("db_name", "kafka_messages.sqlite"),
            )
            try:
                published_messages_table = sqlite_config.get(
                    "published_messages_table",
                    "published_messages",
                )
                create_published_messages_table(db_connection, table_name=published_messages_table)
                insert_row(
                    db_connection,
                    published_messages_table,
                    message_to_row(
                        _format_published_message(
                            topic=topic,
                            payload=payload,
                            key=args.key,
                            headers=headers,
                            delivery=result,
                            scenario_id="s000",
                            file_name="0.json",
                        )
                    ),
                )
                _status(f"Wrote published message to {published_messages_table} table in SQLite")
            finally:
                db_connection.close()
        print(json.dumps(result, indent=2))
        return

    if args.command == "scenario":
        bootstrap_servers = _resolve_bootstrap_servers(config)
        scenario_dir = _resolve_scenario_dir(config, args.config, args.scenario)
        scenario = _load_yaml(scenario_dir / "scenario.yml")
        scenario_id = scenario["scenario_id"]
        delay_seconds = float(scenario.get("delay", 0))
        topic = config["topics"]["inbound"]
        headers = {"source": "mako"}
        publish_config = config.get("cli", {}).get("publish", {})
        producer_config = config.get("producer", {})
        msk_config = config.get("msk", {})
        sqlite_config = config.get("sqlite", {})
        db_connection = None
        published_messages_table = None
        if _should_persist_sqlite(None, sqlite_config):
            db_connection = initialize_db(
                _resolve_run_folder(config, args.config),
                db_name=sqlite_config.get("db_name", "kafka_messages.sqlite"),
            )
            published_messages_table = sqlite_config.get(
                "published_messages_table",
                "published_messages",
            )
            create_published_messages_table(db_connection, table_name=published_messages_table)
            _status(f"Writing scenario publishes to {published_messages_table} table in SQLite")

        try:
            for index, file_name in enumerate(scenario.get("files", [])):
                if index > 0 and delay_seconds > 0:
                    _status(f"Waiting {delay_seconds:g}s before next scenario message")
                    time.sleep(delay_seconds)

                payload_path = scenario_dir / file_name
                payload = _load_json(str(payload_path))
                key = str(payload["id"])
                _status(f"Publishing scenario {scenario_id} file {file_name} to {topic}")
                result = publish_message(
                    payload,
                    topic=topic,
                    bootstrap_servers=bootstrap_servers,
                    client_id=config["clients"]["producer_client_id"],
                    security_protocol=msk_config["security_protocol"],
                    ssl_endpoint_identification_algorithm=msk_config.get("ssl_endpoint_identification_algorithm"),
                    delivery_timeout_ms=int(producer_config["delivery_timeout_ms"]),
                    flush_timeout_seconds=float(publish_config.get("flush_timeout_seconds", 30)),
                    key=key,
                    headers=headers,
                    producer_extra_config=producer_config.get("extra_config"),
                )

                if db_connection is not None and published_messages_table is not None:
                    insert_row(
                        db_connection,
                        published_messages_table,
                        message_to_row(
                            _format_published_message(
                                topic=topic,
                                payload=payload,
                                key=key,
                                headers=headers,
                                delivery=result,
                                scenario_id=scenario_id,
                                file_name=file_name,
                            )
                        ),
                    )

                print(
                    json.dumps(
                        {
                            "scenario_id": scenario_id,
                            "file_name": file_name,
                            **result,
                        },
                        indent=2,
                    )
                )
        finally:
            if db_connection is not None:
                db_connection.close()
        return

    if args.command == "receive":
        bootstrap_servers = _resolve_bootstrap_servers(config)
        receive_config = config.get("cli", {}).get("receive", {})
        consumer_config = config.get("consumer", {})
        msk_config = config.get("msk", {})
        sqlite_config = config.get("sqlite", {})
        db_connection = None
        messages_table = None
        if _should_persist_sqlite(args.persist, sqlite_config):
            db_connection = initialize_db(
                _resolve_run_folder(config, args.config),
                db_name=sqlite_config.get("db_name", "kafka_messages.sqlite"),
            )
            messages_table = sqlite_config.get("received_messages_table", "received_messages")
            create_messages_table(db_connection, table_name=messages_table)
            _status(f"Writing received messages to {messages_table} table in SQLite")
        topic = _resolve_topic(config, args.topic or receive_config.get("topic") or "inbound")
        offset = args.offset if args.offset is not None else int(receive_config.get("offset", -1))
        partition = args.partition if args.partition is not None else int(receive_config.get("partition", 0))
        count = args.count if args.count is not None else receive_config.get("count")
        count = int(count) if count is not None else None
        if count is not None and count < 1:
            raise SystemExit("--count must be greater than 0")
        timeout_seconds = (
            args.timeout if args.timeout is not None else float(receive_config.get("timeout_seconds", 30))
        )
        count_text = str(count) if count is not None else "all available"
        _status(
            f"Receiving from {topic}, partition {partition}, "
            f"offset {offset}, count {count_text}, timeout {timeout_seconds:g}s"
        )
        try:
            for message in iter_messages(
                topic=topic,
                bootstrap_servers=bootstrap_servers,
                group_id=args.consumer_group_id or config["clients"]["consumer_group_id"],
                client_id=config["clients"]["consumer_client_id"],
                security_protocol=msk_config["security_protocol"],
                ssl_endpoint_identification_algorithm=msk_config.get("ssl_endpoint_identification_algorithm"),
                auto_offset_reset=consumer_config["auto_offset_reset"],
                enable_auto_commit=bool(consumer_config["enable_auto_commit"]),
                offset=offset,
                partition=partition,
                limit=count,
                timeout_seconds=timeout_seconds,
                poll_timeout_seconds=float(consumer_config["poll_timeout_seconds"]),
                commit=args.commit if args.commit is not None else bool(receive_config.get("commit", False)),
                consumer_extra_config=consumer_config.get("extra_config"),
            ):
                formatted_message = _format_received_message(message)
                if db_connection is not None and messages_table is not None:
                    insert_row(db_connection, messages_table, message_to_row(formatted_message))
                    print(f"received message offset {message.offset}", flush=True)
                else:
                    print(json.dumps(formatted_message, default=str), flush=True)
        finally:
            if db_connection is not None:
                db_connection.close()
        return

    if args.command == "purge":
        topic_label, topic = _prompt_purge_topic(config)
        bootstrap_servers = _resolve_bootstrap_servers(config)
        msk_config = config.get("msk", {})
        purge_config = config.get("cli", {}).get("purge", {})
        timeout_seconds = float(purge_config.get("timeout_seconds", 60))
        _status(f"Purging configured {topic_label} topic: {topic}")
        results = purge_topic(
            topic=topic,
            bootstrap_servers=bootstrap_servers,
            admin_client_id=config["clients"]["admin_client_id"],
            consumer_group_id=config["clients"]["consumer_group_id"],
            consumer_client_id=config["clients"]["consumer_client_id"],
            security_protocol=msk_config["security_protocol"],
            ssl_endpoint_identification_algorithm=msk_config.get("ssl_endpoint_identification_algorithm"),
            timeout_seconds=timeout_seconds,
            admin_extra_config=purge_config.get("admin_extra_config"),
            consumer_extra_config=config.get("consumer", {}).get("extra_config"),
        )
        print(json.dumps({"topic": topic, "partitions": results}, indent=2))


def _load_json(path: str) -> Any:
    with Path(path).open("r", encoding="utf-8") as handle:
        return json.load(handle)


def _load_yaml(path: Path) -> dict[str, Any]:
    try:
        import yaml
    except ImportError as exc:
        raise RuntimeError("PyYAML is required. Install with `uv sync`.") from exc

    with path.open("r", encoding="utf-8") as handle:
        value = yaml.safe_load(handle) or {}
    if not isinstance(value, dict):
        raise ValueError(f"Expected YAML mapping in {path}")
    return value


def _parse_headers(raw_headers: list[str]) -> dict[str, str]:
    headers: dict[str, str] = {}
    for raw_header in raw_headers:
        if "=" not in raw_header:
            raise ValueError(f"Header must be NAME=VALUE: {raw_header}")
        key, value = raw_header.split("=", 1)
        headers[key] = value
    return headers


def _resolve_topic(config: dict[str, Any], topic: str) -> str:
    return config.get("topics", {}).get(topic, topic)


def _should_persist_sqlite(persist_option: str | None, sqlite_config: dict[str, Any]) -> bool:
    return persist_option == "sqlite" or bool(sqlite_config.get("auto_perist", False))


def _resolve_bootstrap_servers(config: dict[str, Any]) -> str:
    _status(_aws_source_message(config))
    bootstrap_servers = get_bootstrap_servers(config)
    _status("Resolved MSK bootstrap brokers")
    return bootstrap_servers


def _prompt_purge_topic(config: dict[str, Any]) -> tuple[str, str]:
    inbound_topic = config["topics"]["inbound"]
    outbound_topic = config["topics"]["outbound"]
    print("Select topic to purge:")
    print(f"(1) inbound  - {inbound_topic}")
    print(f"(2) outbound - {outbound_topic}")
    choice = input("Enter your choice: ").strip()
    if choice == "1":
        return "inbound", inbound_topic
    if choice == "2":
        return "outbound", outbound_topic
    raise SystemExit("Invalid choice. Purge cancelled.")


def _resolve_run_folder(config: dict[str, Any], config_path: str | None) -> Path:
    run_folder = Path(config["run_folder"]).expanduser()
    if run_folder.is_absolute():
        return run_folder

    base_path = Path(config_path).expanduser().resolve().parent if config_path else DEFAULT_CONFIG_PATH.parent
    return base_path / run_folder


def _resolve_scenario_dir(config: dict[str, Any], config_path: str | None, scenario_id: str) -> Path:
    base_path = Path(config_path).expanduser().resolve().parent if config_path else DEFAULT_CONFIG_PATH.parent
    scenario_dir = base_path / "scenarios" / scenario_id
    scenario_file = scenario_dir / "scenario.yml"
    if not scenario_file.exists():
        raise SystemExit(f"Scenario not found: {scenario_id}")
    return scenario_dir


def _format_received_message(message: KafkaMessage) -> dict[str, Any]:
    timestamp_millis = _timestamp_millis(message)
    return {
        "topic": message.topic,
        "key": message.key,
        "origin": _message_origin(message),
        "offset": message.offset,
        "partition": message.partition,
        "timestamp": timestamp_millis,
        "datetime": _format_timestamp(timestamp_millis),
        "value": message.value,
    }


def _format_published_message(
    *,
    topic: str,
    payload: Any,
    key: str | None,
    headers: dict[str, Any] | None,
    delivery: dict[str, Any],
    scenario_id: str = "s000",
    file_name: str = "0.json",
) -> dict[str, Any]:
    timestamp_millis = int(datetime.now(UTC).timestamp() * 1000)
    return {
        "topic": topic,
        "key": delivery.get("key") or key or _payload_default_key(payload),
        "origin": _headers_origin(headers) or (payload.get("origin") if isinstance(payload, dict) else None),
        "offset": delivery.get("offset"),
        "partition": delivery.get("partition"),
        "timestamp": timestamp_millis,
        "datetime": _format_timestamp(timestamp_millis),
        "value": payload,
        "scenario_id": scenario_id,
        "file_name": file_name,
    }


def _payload_default_key(payload: Any) -> str | None:
    if isinstance(payload, dict) and payload.get("id"):
        return str(payload["id"])
    return None


def _headers_origin(headers: dict[str, Any] | None) -> Any:
    if not headers:
        return None
    if "origin" in headers:
        return headers["origin"]
    return headers.get("source")


def _timestamp_millis(message: KafkaMessage) -> int | None:
    if len(message.timestamp) < 2 or message.timestamp[1] < 0:
        return None
    return message.timestamp[1]


def _format_timestamp(timestamp_millis: int | None) -> str | None:
    if timestamp_millis is None:
        return None
    return datetime.fromtimestamp(timestamp_millis / 1000, UTC).strftime("%Y-%m-%dT%H:%M:%SZ")


def _message_origin(message: KafkaMessage) -> Any:
    if "origin" in message.headers:
        return message.headers["origin"]
    if "source" in message.headers:
        return message.headers["source"]
    if isinstance(message.value, dict):
        return message.value.get("origin")
    return None


def _status(message: str) -> None:
    print(f"[kafka-automation] {message}", file=sys.stderr, flush=True)


def _aws_source_message(config: dict[str, Any]) -> str:
    aws_config = config.get("aws", {})
    source = config.get("msk", {}).get("parameter_source", "secretsmanager")
    credentials_file = Path(aws_config["credentials_file"]) if aws_config.get("credentials_file") else DEFAULT_AWS_CREDS_PATH
    profile = aws_config.get("profile")
    if credentials_file.exists():
        return f"Fetching MSK bootstrap brokers from AWS {source} using {credentials_file}"
    return f"Fetching MSK bootstrap brokers from AWS {source} using profile {profile}"


if __name__ == "__main__":
    main()
