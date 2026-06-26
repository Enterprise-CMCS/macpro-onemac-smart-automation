# Kafka Automation

Python helpers for interacting with the BigMAC MSK cluster used by OneMAC/SMART
automation.

The provided `kafka-ui-local.sh` script reads broker addresses from AWS Secrets
Manager at `bigmac/{stage}/brokerString` using AWS profile `bigmac`. This project
uses the same default and keeps it configurable in `config.yml`.
Set `msk.parameter_source` to `ssm` if the broker string moves to Parameter
Store.

## Setup

```bash
cd kafka-automation
uv sync
```

You need VPN/network access to MSK and AWS credentials for the configured
profile. Keep `uv.lock` committed so everyone installs the same dependency
versions.

For temporary AWS credentials, create `kafka-automation/.awscreds`:

```bash
aws_access_key_id=
aws_secret_access_key=
aws_session_token=
```

The `.awscreds` file is ignored by git. If it exists, the utility uses it for
AWS Secrets Manager/SSM calls; otherwise it uses the AWS profile from
`config.yml`.

## Configuration

Edit `config.yml` or override `env`/`user` at runtime. Supported `env` values
are `dev` and `qa`.

Important defaults:

- AWS profile: `bigmac`
- AWS region: `us-east-1`
- Dev MSK broker secret: `bigmac/master/brokerString`
- QA MSK broker secret: `bigmac/val/brokerString`
- Dev inbound topic: `aws.onemac.migration.cdc.devtest`
- QA inbound topic: `aws.onemac.migration.cdc.qatest`
- Dev outbound topic: `aws.mulesoft.onemac.events.devtest`
- QA outbound topic: `aws.mulesoft.onemac.events.qatest`
- Consumer group: `onemac-smart-automation-{env}-{user}`
- Producer client: `onemac-smart-automation-producer-{env}-{user}`
- Admin client: `onemac-smart-automation-admin-{env}-{user}`
- SQLite database: `{run_folder}/kafka_messages.sqlite`

Client identifiers use the short env value, for example `dev-your-name`.

## Import From Another Project

```python
from kafka_utils import publish_message, receive_message
from utils import get_bootstrap_servers, load_config

config = load_config("config.yml")
bootstrap_servers = get_bootstrap_servers(config)

payload = {
    "event": "new-medicaid-submission",
    "id": "ZZ-26-0001",
    "origin": "mako",
}

delivery = publish_message(
    payload,
    topic=config["topics"]["inbound"],
    bootstrap_servers=bootstrap_servers,
    client_id=config["clients"]["producer_client_id"],
    security_protocol=config["msk"]["security_protocol"],
    ssl_endpoint_identification_algorithm=config["msk"].get("ssl_endpoint_identification_algorithm"),
    delivery_timeout_ms=config["producer"]["delivery_timeout_ms"],
    flush_timeout_seconds=config["cli"]["publish"]["flush_timeout_seconds"],
    headers=config["cli"]["publish"]["headers"],
)

response = receive_message(
    topic=config["topics"]["outbound"],
    bootstrap_servers=bootstrap_servers,
    group_id=config["clients"]["consumer_group_id"],
    client_id=config["clients"]["consumer_client_id"],
    security_protocol=config["msk"]["security_protocol"],
    ssl_endpoint_identification_algorithm=config["msk"].get("ssl_endpoint_identification_algorithm"),
    auto_offset_reset=config["consumer"]["auto_offset_reset"],
    enable_auto_commit=config["consumer"]["enable_auto_commit"],
    partition=config["cli"]["receive"]["partition"],
    offset=config["cli"]["receive"]["offset"],
    timeout_seconds=config["cli"]["receive"]["timeout_seconds"],
    poll_timeout_seconds=config["consumer"]["poll_timeout_seconds"],
    commit=config["cli"]["receive"]["commit"],
)
```

`publish_message` defaults the key to `payload["id"]` and the headers to
`{"origin": "mako"}` only when those arguments are omitted. The CLI passes
resolved values from `config.yml`.

## CLI

Publish a sample inbound message:

```bash
uv run kafka-automation publish examples/new-medicaid-submission.json
```

Publish and persist the published message metadata/payload to SQLite:

```bash
uv run kafka-automation publish --topic aws.mulesoft.onemac.events.devtest --persist sqlite examples/new-medicaid-submission.json
```

SQLite persistence is automatically enabled when `sqlite.auto_perist` is `true`
in `config.yml`. If that setting is `false`, pass `--persist sqlite` to persist
for a single command. Published messages use the configured SQLite table
`published_messages_table`. The default table is `published_messages`.

Publish all payloads for a scenario:

```bash
uv run kafka-automation scenario --scenario s001
```

Scenario publishing reads `kafka-automation/scenarios/<scenario_id>/scenario.yml`
and publishes the listed JSON files in order to the configured inbound topic.
For each scenario message, the Kafka key is the payload `id`, and the header is
`{"source": "mako"}` as described in `references/medicaid-spa.md`.

When SQLite persistence is enabled, rows written to `published_messages` include
the scenario context:

- `scenario_id`, for example `s001`
- `file_name`, for example `1.json`

For ordinary `publish` commands outside a scenario, those values default to
`s000` and `0.json`.

Receive messages from the configured outbound topic using the configured
consumer group, partition, offset behavior, and timeout:

```bash
uv run kafka-automation receive
```

By default, `receive` reads from the configured outbound topic. For `dev`, that
is `aws.mulesoft.onemac.events.devtest`. You can read from any topic with
`--topic`. For example, to receive from the dev publish/inbound topic:

```bash
uv run kafka-automation receive --topic aws.onemac.migration.cdc.devtest
```

Without `--count`, `receive` reads everything it can from the starting offset
until the configured timeout is reached. Use `--count` to stop after a specific
number of messages. If fewer messages are available than the requested count,
the command returns the messages it found after the topic goes idle. If no
messages are available at all, it waits until the receive timeout.

```bash
uv run kafka-automation receive --topic aws.onemac.migration.cdc.devtest --count 12
```

To read 12 messages starting from a specific offset:

```bash
uv run kafka-automation receive --topic aws.onemac.migration.cdc.devtest --count 12 --offset 8000
```

Each received message is printed as one JSON object per line:

```json
{"topic":"aws.onemac.migration.cdc.devtest","key":"ZZ-26-0001","origin":"mako","offset":8000,"partition":0,"timestamp":1781109682195,"datetime":"2026-06-10T16:41:22Z","value":{"id":"ZZ-26-0001"}}
```

The `timestamp` field is the Kafka timestamp in milliseconds. The `datetime`
field is the same timestamp formatted as ISO-8601 UTC.

Received messages are persisted automatically when `sqlite.auto_perist` is
`true`. If that setting is `false`, pass `--persist sqlite` to persist for a
single command:

```bash
uv run kafka-automation receive --topic aws.onemac.migration.cdc.devtest --count 12 --persist sqlite
```

When persistence is enabled, whether by config or CLI flag, the terminal prints
lightweight progress instead of the full message:

```text
received message offset 8000
```

SQLite writes go to the configured `run_folder`. The default database is
`kafka_messages.sqlite`, and the default table is `received_messages`.
Received messages use the configured SQLite table `received_messages_table`.

The `received_messages` table columns are:

- `topic` as `VARCHAR2(200)`
- `key` as `VARCHAR2(25)`
- `origin` as `VARCHAR2(25)`
- `offset` as `INTEGER`
- `partition` as `INTEGER`
- `timestamp` as `LONG`
- `datetime` as `DATETIME`
- `value` as `TEXT`

SQLite stores values dynamically, so `DATETIME` is also stored as ISO-8601 UTC
text like `2026-06-10T16:41:22Z`. The `timestamp` column keeps the original
Kafka timestamp in milliseconds.

Read an exact offset. Topic and partition still come from `config.yml`:

```bash
uv run kafka-automation receive --offset 12345
```

When `--offset -1` is used, the utility subscribes normally and Kafka uses the
consumer group identifier from `config.yml`. For a new group and
`auto_offset_reset: earliest`, Kafka starts from the beginning of the topic.

Delete all currently available records from one configured topic:

```bash
uv run kafka-automation purge
```

The command only allows the configured inbound or outbound topic:

```text
Select topic to purge:
(1) inbound  - aws.onemac.migration.cdc.devtest
(2) outbound - aws.mulesoft.onemac.events.devtest
Enter your choice:
```

Purging deletes records up to each partition's current high watermark. Messages
published after the purge starts are not part of that delete request.
