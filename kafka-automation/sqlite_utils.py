from __future__ import annotations

import json
import sqlite3
from datetime import UTC, datetime
from pathlib import Path
from typing import Any


MESSAGE_COLUMNS = {
    "topic": "VARCHAR2(200)",
    "key": "VARCHAR2(25)",
    "origin": "VARCHAR2(25)",
    "offset": "INTEGER",
    "partition": "INTEGER",
    "timestamp": "LONG",
    "datetime": "DATETIME",
    "value": "TEXT",
}

PUBLISHED_MESSAGE_COLUMNS = {
    **MESSAGE_COLUMNS,
    "scenario_id": "VARCHAR2(25)",
    "file_name": "VARCHAR2(100)",
}


def initialize_db(
    run_folder: str | Path,
    *,
    db_name: str = "kafka_messages.sqlite",
) -> sqlite3.Connection:
    """Create the run folder and open the SQLite database."""

    run_path = Path(run_folder).expanduser()
    run_path.mkdir(parents=True, exist_ok=True)
    connection = sqlite3.connect(run_path / db_name)
    connection.row_factory = sqlite3.Row
    return connection


def create_table(
    connection: sqlite3.Connection,
    table_name: str,
    columns: dict[str, str],
) -> None:
    """Create a table if it does not already exist."""

    column_sql = ", ".join(f"{_quote_identifier(name)} {data_type}" for name, data_type in columns.items())
    connection.execute(f"CREATE TABLE IF NOT EXISTS {_quote_identifier(table_name)} ({column_sql})")
    _add_missing_columns(connection, table_name, columns)
    connection.commit()


def create_messages_table(
    connection: sqlite3.Connection,
    *,
    table_name: str = "messages",
) -> None:
    """Create the default Kafka messages table."""

    create_table(connection, table_name, MESSAGE_COLUMNS)


def create_published_messages_table(
    connection: sqlite3.Connection,
    *,
    table_name: str = "published_messages",
) -> None:
    """Create the default published Kafka messages table."""

    create_table(connection, table_name, PUBLISHED_MESSAGE_COLUMNS)


def insert_row(
    connection: sqlite3.Connection,
    table_name: str,
    row: dict[str, Any],
) -> None:
    """Insert one row into a table."""

    if not row:
        raise ValueError("row must contain at least one value")

    columns = list(row)
    placeholders = ", ".join("?" for _ in columns)
    column_sql = ", ".join(_quote_identifier(column) for column in columns)
    values = [_serialize_value(row[column]) for column in columns]

    connection.execute(
        f"INSERT INTO {_quote_identifier(table_name)} ({column_sql}) VALUES ({placeholders})",
        values,
    )
    connection.commit()


def message_to_row(message: dict[str, Any]) -> dict[str, Any]:
    """Convert a formatted Kafka message into the messages table shape."""

    row = {
        "topic": message.get("topic"),
        "key": message.get("key"),
        "origin": message.get("origin"),
        "offset": message.get("offset"),
        "partition": message.get("partition"),
        "timestamp": message.get("timestamp"),
        "datetime": _datetime_from_timestamp(message.get("timestamp")) or message.get("datetime"),
        "value": _serialize_value(message.get("value")),
    }
    if "scenario_id" in message:
        row["scenario_id"] = message.get("scenario_id")
    if "file_name" in message:
        row["file_name"] = message.get("file_name")
    return row


def _add_missing_columns(
    connection: sqlite3.Connection,
    table_name: str,
    columns: dict[str, str],
) -> None:
    existing_columns = {
        row["name"] for row in connection.execute(f"PRAGMA table_info({_quote_identifier(table_name)})")
    }
    for column_name, data_type in columns.items():
        if column_name not in existing_columns:
            connection.execute(
                f"ALTER TABLE {_quote_identifier(table_name)} "
                f"ADD COLUMN {_quote_identifier(column_name)} {data_type}"
            )


def _datetime_from_timestamp(timestamp_millis: Any) -> str | None:
    if timestamp_millis is None:
        return None
    try:
        timestamp_seconds = int(timestamp_millis) / 1000
    except (TypeError, ValueError):
        return None
    return datetime.fromtimestamp(timestamp_seconds, UTC).strftime("%Y-%m-%dT%H:%M:%SZ")


def _serialize_value(value: Any) -> Any:
    if isinstance(value, (dict, list)):
        return json.dumps(value, separators=(",", ":"), default=str)
    return value


def _quote_identifier(identifier: str) -> str:
    return '"' + identifier.replace('"', '""') + '"'
