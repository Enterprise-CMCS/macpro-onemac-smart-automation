# Copilot Instructions — OneMAC SMART Automation

## Project Overview

This repository contains the **OneMAC SMART Automation Framework** with two main components:

1. **UI Automation** (Java/Selenium/TestNG) — located under `src/`, uses Page Object Model, data-driven testing with Excel-backed test data, and dynamic package ID generation.
2. **Kafka Message Automation** (Python) — located under `kafka-automation/`, used for publishing and consuming messages on the BigMAC MSK cluster.

## Kafka Automation

The `kafka-automation/` folder contains Python-based tooling for Kafka message automation testing against the OneMAC MSK cluster. For full setup, configuration, and usage details refer to [kafka-automation/README.md](../kafka-automation/README.md).

### Key Files

- `cli.py` — CLI entry point exposing commands: `publish`, `receive`, `scenario`, and `purge`.
- `kafka_utils.py` — Low-level Kafka producer/consumer helpers.
- `sqlite_utils.py` — SQLite persistence for published/received messages.
- `utils.py` — Config loading and AWS bootstrap server resolution.
- `config.yml` — Runtime configuration (environment, topics, client IDs, timeouts).

### CLI Commands

```bash
# Publish a single JSON payload
kafka-automation publish <payload_file> [--topic TOPIC] [--key KEY] [--header NAME=VALUE]

# Publish all messages for a scenario
kafka-automation scenario --scenario s001

# Receive messages from a topic
kafka-automation receive [--topic TOPIC] [--count N] [--offset N] [--timeout SECS]

# Purge all messages from a topic
kafka-automation purge
```

### Scenarios

Test scenarios live in `kafka-automation/scenarios/` as numbered subfolders (`s001`, `s002`, …). Each scenario contains:

- `scenario.yml` — metadata (scenario_id, id, description, delay, files list).
- `1.json`, `2.json`, … — ordered message payloads.

### Generating New Scenarios

Instructions for generating new scenarios are in [kafka-automation/references/scenario-generation.md](../kafka-automation/references/scenario-generation.md). Key rules:

- Each scenario folder targets a single SPA; all payloads share the same SPA/package identifiers.
- Auto-increment the SPA ID (e.g., `ZZ-26-0002` when `ZZ-26-0001` is already in use).
- `timestamp` and `uploadDate` fields use current time in milliseconds.
- `proposedEffectiveDate` should be a future date (1–2 months ahead) in milliseconds.
- The scenario inventory is tracked in `kafka-automation/scenarios/inventory.md`.

### Message Templates

Common inbound and outbound message templates (new submission, subsequent documents, withdrawal) are documented in [kafka-automation/references/medicaid-spa.md](../kafka-automation/references/medicaid-spa.md). Use these as a starting point when generating scenario payloads.

### Environments

| Setting | Dev | QA |
|---|---|---|
| Inbound topic | `aws.onemac.migration.cdc.devtest` | `aws.onemac.migration.cdc.qatest` |
| Outbound topic | `aws.mulesoft.onemac.events.devtest` | `aws.mulesoft.onemac.events.qatest` |
| Broker secret | `bigmac/master/brokerString` | `bigmac/val/brokerString` |

## UI Automation (Java)

- **Pages** (`src/main/java/gov/cms/smart/pages/`) — Page Object Model classes.
- **Models** (`src/main/java/gov/cms/smart/models/`) — SPA and Waiver POJOs.
- **Utils** (`src/main/java/gov/cms/smart/utils/`) — Driver factory, config, Excel trackers, ID generators.
- **Tests** (`src/test/java/gov/cms/smart/tests/`) — TestNG test classes.
- Package IDs are generated via `SpaIdGenerator` and `WaiverIdGenerator`, tracked in `packages.xlsx` and `state_counters.xlsx`.
