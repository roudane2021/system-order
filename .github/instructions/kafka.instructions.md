# Kafka — conventions, résilience et bonnes pratiques

But
- Standardiser l'usage de Kafka pour l'architecture event-driven et fournir des règles pour la conception d'événements, la résilience et l'observabilité.

Top-level rules
- Use a Schema Registry (Avro / JSON Schema / Protobuf) with subjects named `<topic>-value`.
- Include metadata fields in every event: `eventId` (UUID), `occurredAt` (ISO8601), `source` (service), `schemaVersion`/`eventVersion`.

Topic naming
- Format : `<env>.<domain>.<aggregate>.<action>.v<major>`
  - Ex: `prod.order.order.created.v1`, `dev.inventory.stock.reserved.v1`.
- Keep names lowercase and use dots for segmentation.

Event versioning
- Use semantic naming with major version in topic for breaking changes.
- Backwards-compatible changes → schema evolution without changing topic.

Producer responsibilities
- Publish event only after successful transaction commit (transactional outbox pattern recommended if immediate consistency required).
- Set headers: `traceId`, `spanId`, `eventId`.
- Do not embed sensitive secrets in event payload.

Consumer responsibilities
- Be idempotent: keep a dedup store keyed by `eventId` (Redis, DB table) with appropriate TTL.
- Implement retry with exponential backoff; after N attempts, route event to DLQ.
- Validate schema and handle unknown fields gracefully.

Dead Letter Queue (DLQ)
- DLQ naming: `<original-topic>.dlq` or `<env>.<domain>.<aggregate>.<action>.dlq.v1`.
- When moving to DLQ, include `errorReason`, `attempts`, `originalHeaders`.
- Monitor DLQ and alert on growth.

Retry strategy
- Prefer consumer-side retry via backoff + small bounded retry counter.
- Avoid infinite retries. Use DLQ for manual investigation or automated reprocessing tools.

Consumer Group naming
- Format: `<env>.<service>.<purpose>` (ex: `prod.infra-notification.default`).
- Ensure group names map to deploy/environment and are stable across restarts.

Schema evolution
- Follow compatibility rules per message format (backward, forward, full).
- For Avro: prefer backward compatibility for consumers.

Idempotency & dedup
- Include unique `eventId`. Consumers store processed `eventId` for dedup.
- Use consistent hashing/partitioning keys for ordering if order matters (e.g., `orderId`).

Observability
- Emit metrics: messages_consumed_total, messages_failed_total, messages_dropped_total, consumer_lag.
- Push trace headers and ensure tracing systems link producer/consumer spans.

Anti‑patterns
- Using Kafka as a database.
- Tight coupling between producer schema and consumer DB schema.
- Ignoring DLQ and failed messages.

Testing
- Use `spring-kafka-test` or Testcontainers Kafka for integration tests.

Examples (producer snippet)
```java
ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>(topic, key, payload);
record.headers().add("traceId", traceId.getBytes(StandardCharsets.UTF_8));
kafkaTemplate.send(record);
```

--

Fichier: `/opt/backend/system-order/.github/instructions/kafka.instructions.md`
