from __future__ import annotations

import json
import time
from dataclasses import dataclass
from typing import Any, Iterator

from utils import KafkaAutomationError


@dataclass(frozen=True)
class KafkaMessage:
    topic: str
    partition: int
    offset: int
    key: str | None
    value: Any
    headers: dict[str, Any]
    timestamp: tuple[int, int]


def create_consumer(
    *,
    bootstrap_servers: str,
    group_id: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    auto_offset_reset: str,
    enable_auto_commit: bool,
    extra_config: dict[str, Any] | None = None,
):
    """Create a confluent-kafka Consumer from explicit settings."""

    try:
        from confluent_kafka import Consumer
    except ImportError as exc:
        raise KafkaAutomationError("confluent-kafka is required. Install with `uv sync`.") from exc

    config = {
        "bootstrap.servers": bootstrap_servers,
        "group.id": group_id,
        "client.id": client_id,
        "security.protocol": security_protocol,
        "auto.offset.reset": auto_offset_reset,
        "enable.auto.commit": enable_auto_commit,
    }
    if ssl_endpoint_identification_algorithm is not None:
        config["ssl.endpoint.identification.algorithm"] = ssl_endpoint_identification_algorithm
    if extra_config:
        config.update(extra_config)

    return Consumer(config)


def create_producer(
    *,
    bootstrap_servers: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    delivery_timeout_ms: int,
    extra_config: dict[str, Any] | None = None,
):
    """Create a confluent-kafka Producer from explicit settings."""

    try:
        from confluent_kafka import Producer
    except ImportError as exc:
        raise KafkaAutomationError("confluent-kafka is required. Install with `uv sync`.") from exc

    config = {
        "bootstrap.servers": bootstrap_servers,
        "client.id": client_id,
        "security.protocol": security_protocol,
        "delivery.timeout.ms": delivery_timeout_ms,
    }
    if ssl_endpoint_identification_algorithm is not None:
        config["ssl.endpoint.identification.algorithm"] = ssl_endpoint_identification_algorithm
    if extra_config:
        config.update(extra_config)

    return Producer(config)


def create_admin_client(
    *,
    bootstrap_servers: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    extra_config: dict[str, Any] | None = None,
):
    """Create a confluent-kafka AdminClient from explicit settings."""

    try:
        from confluent_kafka.admin import AdminClient
    except ImportError as exc:
        raise KafkaAutomationError("confluent-kafka is required. Install with `uv sync`.") from exc

    config = {
        "bootstrap.servers": bootstrap_servers,
        "client.id": client_id,
        "security.protocol": security_protocol,
    }
    if ssl_endpoint_identification_algorithm is not None:
        config["ssl.endpoint.identification.algorithm"] = ssl_endpoint_identification_algorithm
    if extra_config:
        config.update(extra_config)

    return AdminClient(config)


def purge_topic(
    *,
    topic: str,
    bootstrap_servers: str,
    admin_client_id: str,
    consumer_group_id: str,
    consumer_client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    timeout_seconds: float,
    admin_extra_config: dict[str, Any] | None = None,
    consumer_extra_config: dict[str, Any] | None = None,
) -> list[dict[str, int]]:
    """Delete all currently available records from a topic."""

    try:
        from confluent_kafka import TopicPartition
    except ImportError as exc:
        raise KafkaAutomationError("confluent-kafka is required. Install with `uv sync`.") from exc

    admin_client = create_admin_client(
        bootstrap_servers=bootstrap_servers,
        client_id=admin_client_id,
        security_protocol=security_protocol,
        ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
        extra_config=admin_extra_config,
    )
    consumer = create_consumer(
        bootstrap_servers=bootstrap_servers,
        group_id=consumer_group_id,
        client_id=consumer_client_id,
        security_protocol=security_protocol,
        ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
        auto_offset_reset="earliest",
        enable_auto_commit=False,
        extra_config=consumer_extra_config,
    )

    try:
        metadata = consumer.list_topics(topic=topic, timeout=timeout_seconds)
        topic_metadata = metadata.topics.get(topic)
        if topic_metadata is None:
            raise KafkaAutomationError(f"Topic not found: {topic}")
        if topic_metadata.error is not None:
            raise KafkaAutomationError(f"Unable to read topic metadata for {topic}: {topic_metadata.error}")

        partition_offsets = []
        purge_results: list[dict[str, int]] = []
        for partition in sorted(topic_metadata.partitions):
            topic_partition = TopicPartition(topic, partition)
            low_offset, high_offset = consumer.get_watermark_offsets(
                topic_partition,
                timeout=timeout_seconds,
            )
            purge_results.append(
                {
                    "partition": partition,
                    "low_offset": low_offset,
                    "high_offset": high_offset,
                    "delete_before_offset": high_offset,
                }
            )
            partition_offsets.append(TopicPartition(topic, partition, high_offset))

        if not hasattr(admin_client, "delete_records"):
            raise KafkaAutomationError("Installed confluent-kafka version does not support delete_records.")

        futures = admin_client.delete_records(partition_offsets, request_timeout=timeout_seconds)
        for topic_partition, future in futures.items():
            future.result(timeout=timeout_seconds)
            for result in purge_results:
                if result["partition"] == topic_partition.partition:
                    result["deleted_to_offset"] = topic_partition.offset
                    break

        return purge_results
    finally:
        consumer.close()


def receive_message(
    *,
    topic: str,
    bootstrap_servers: str,
    group_id: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    auto_offset_reset: str,
    enable_auto_commit: bool,
    partition: int,
    offset: int,
    timeout_seconds: float,
    poll_timeout_seconds: float,
    commit: bool,
    consumer_extra_config: dict[str, Any] | None = None,
) -> KafkaMessage | None:
    """Receive one message using fully supplied Kafka settings."""

    return next(
        iter_messages(
            topic=topic,
            bootstrap_servers=bootstrap_servers,
            group_id=group_id,
            client_id=client_id,
            security_protocol=security_protocol,
            ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
            auto_offset_reset=auto_offset_reset,
            enable_auto_commit=enable_auto_commit,
            partition=partition,
            offset=offset,
            limit=1,
            timeout_seconds=timeout_seconds,
            poll_timeout_seconds=poll_timeout_seconds,
            commit=commit,
            consumer_extra_config=consumer_extra_config,
        ),
        None,
    )


def receive_messages(
    *,
    topic: str,
    bootstrap_servers: str,
    group_id: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    auto_offset_reset: str,
    enable_auto_commit: bool,
    partition: int,
    offset: int,
    limit: int | None,
    timeout_seconds: float,
    poll_timeout_seconds: float,
    commit: bool,
    consumer_extra_config: dict[str, Any] | None = None,
) -> list[KafkaMessage]:
    """Receive messages into a list."""

    return list(
        iter_messages(
            topic=topic,
            bootstrap_servers=bootstrap_servers,
            group_id=group_id,
            client_id=client_id,
            security_protocol=security_protocol,
            ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
            auto_offset_reset=auto_offset_reset,
            enable_auto_commit=enable_auto_commit,
            partition=partition,
            offset=offset,
            limit=limit,
            timeout_seconds=timeout_seconds,
            poll_timeout_seconds=poll_timeout_seconds,
            commit=commit,
            consumer_extra_config=consumer_extra_config,
        )
    )


def iter_messages(
    *,
    topic: str,
    bootstrap_servers: str,
    group_id: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    auto_offset_reset: str,
    enable_auto_commit: bool,
    partition: int,
    offset: int,
    limit: int | None,
    timeout_seconds: float,
    poll_timeout_seconds: float,
    commit: bool,
    consumer_extra_config: dict[str, Any] | None = None,
) -> Iterator[KafkaMessage]:
    """
    Yield messages from Kafka.

    offset >= 0 reads from the exact topic partition offset.
    offset == -1 subscribes normally and lets Kafka use the supplied consumer group.
    """

    try:
        from confluent_kafka import KafkaError, TopicPartition
    except ImportError as exc:
        raise KafkaAutomationError("confluent-kafka is required. Install with `uv sync`.") from exc

    consumer = create_consumer(
        bootstrap_servers=bootstrap_servers,
        group_id=group_id,
        client_id=client_id,
        security_protocol=security_protocol,
        ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
        auto_offset_reset=auto_offset_reset,
        enable_auto_commit=enable_auto_commit,
        extra_config=consumer_extra_config,
    )

    try:
        if offset >= 0:
            consumer.assign([TopicPartition(topic, partition, offset)])
        else:
            consumer.subscribe([topic])

        read_count = 0
        has_read_message = False
        deadline = time.monotonic() + timeout_seconds

        while (limit is None or read_count < limit) and time.monotonic() < deadline:
            remaining = max(0.1, min(poll_timeout_seconds, deadline - time.monotonic()))
            raw_message = consumer.poll(remaining)
            if raw_message is None:
                if has_read_message:
                    break
                continue

            error = raw_message.error()
            if error:
                if error.code() == KafkaError._PARTITION_EOF:
                    continue
                raise KafkaAutomationError(f"Kafka consume error: {error}")

            has_read_message = True
            read_count += 1
            decoded_message = _decode_message(raw_message)
            if commit:
                consumer.commit(raw_message, asynchronous=False)
            yield decoded_message

    finally:
        consumer.close()


def publish_message(
    payload: Any,
    *,
    topic: str,
    bootstrap_servers: str,
    client_id: str,
    security_protocol: str,
    ssl_endpoint_identification_algorithm: str | None,
    delivery_timeout_ms: int,
    flush_timeout_seconds: float,
    key: str | None = None,
    headers: dict[str, Any] | None = None,
    producer_extra_config: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """Publish a message using fully supplied Kafka settings."""

    producer = create_producer(
        bootstrap_servers=bootstrap_servers,
        client_id=client_id,
        security_protocol=security_protocol,
        ssl_endpoint_identification_algorithm=ssl_endpoint_identification_algorithm,
        delivery_timeout_ms=delivery_timeout_ms,
        extra_config=producer_extra_config,
    )
    delivery: dict[str, Any] = {}

    if key is None:
        key = _payload_default_key(payload)

    def delivery_callback(error: Any, message: Any) -> None:
        if error is not None:
            delivery["error"] = str(error)
            return
        delivery.update(
            {
                "topic": message.topic(),
                "partition": message.partition(),
                "offset": message.offset(),
                "key": _decode_bytes(message.key()),
            }
        )

    producer.produce(
        topic,
        key=key.encode("utf-8") if isinstance(key, str) else key,
        value=_payload_to_bytes(payload),
        headers=_headers_to_kafka({"origin": "mako"} if headers is None else headers),
        callback=delivery_callback,
    )
    producer.flush(flush_timeout_seconds)

    if "error" in delivery:
        raise KafkaAutomationError(f"Kafka publish failed: {delivery['error']}")
    if not delivery:
        raise KafkaAutomationError("Kafka publish timed out before delivery acknowledgement.")
    return delivery


def _decode_message(message: Any) -> KafkaMessage:
    return KafkaMessage(
        topic=message.topic(),
        partition=message.partition(),
        offset=message.offset(),
        key=_decode_bytes(message.key()),
        value=_decode_payload(message.value()),
        headers=_decode_headers(message.headers()),
        timestamp=message.timestamp(),
    )


def _decode_payload(value: bytes | None) -> Any:
    text = _decode_bytes(value)
    if text is None:
        return None
    try:
        return json.loads(text)
    except json.JSONDecodeError:
        return text


def _decode_headers(headers: list[tuple[str, bytes | None]] | None) -> dict[str, Any]:
    if not headers:
        return {}
    decoded: dict[str, Any] = {}
    for key, value in headers:
        decoded_value = _decode_bytes(value)
        if decoded_value is None:
            decoded[key] = None
            continue
        try:
            decoded[key] = json.loads(decoded_value)
        except json.JSONDecodeError:
            decoded[key] = decoded_value
    return decoded


def _decode_bytes(value: bytes | None) -> str | None:
    return value.decode("utf-8") if isinstance(value, bytes) else value


def _payload_to_bytes(payload: Any) -> bytes:
    if isinstance(payload, bytes):
        return payload
    if isinstance(payload, str):
        return payload.encode("utf-8")
    return json.dumps(payload, separators=(",", ":"), default=str).encode("utf-8")


def _payload_default_key(payload: Any) -> str:
    if isinstance(payload, dict) and payload.get("id"):
        return str(payload["id"])
    raise ValueError("key is required when payload is not a dict with a non-empty `id` field.")


def _headers_to_kafka(headers: dict[str, Any]) -> list[tuple[str, bytes | None]]:
    kafka_headers: list[tuple[str, bytes | None]] = []
    for key, value in headers.items():
        if value is None:
            kafka_headers.append((key, None))
        elif isinstance(value, bytes):
            kafka_headers.append((key, value))
        elif isinstance(value, str):
            kafka_headers.append((key, value.encode("utf-8")))
        else:
            kafka_headers.append((key, json.dumps(value, separators=(",", ":")).encode("utf-8")))
    return kafka_headers
