# Observability — logs, metrics et tracing

But
- Rendre les services observables par défaut : logs structurés, traces distribuées et metrics exportés.

Logging
- Format structuré (JSON) recommandé en prod.
- Inclure : `timestamp`, `service`, `level`, `traceId`, `spanId`, `userId` (si présent), `message`, `context`.
- Niveaux : ERROR/WARN/INFO/DEBUG. Eviter DEBUG en prod.

Tracing
- OpenTelemetry (ou Spring Cloud Sleuth) pour traces distribuées.
- Propager contexte via HTTP headers (`traceparent` / `b3`) et Kafka headers.
- Correlate logs with traceId and spanId.

Metrics
- Micrometer + Prometheus exposition via `/actuator/prometheus`.
- Nommer metrics suivant convention `service.operation.metric`.
- Exposer business metrics (orders.created, payments.failed) + infra metrics (jvm, gc, db.connections).

Dashboards & alerts
- Grafana dashboards par service : latency, error rate, throughput, consumer lag.
- Alerts examples :
  - consumer lag > threshold
  - error rate spike > X% sustained for 5 minutes
  - high GC pause time

Tracing + Logs example
- Ensure producers include `traceId` header on kafka messages so consumer spans are linked to producer.

Retention & storage
- Define retention for logs/metrics/traces according to compliance and cost.

Security & privacy
- Mask PII in logs. Never log secrets.

Example log payload
```json
{
  "ts":"2026-05-27T12:00:00Z",
  "service":"infra-notification",
  "level":"INFO",
  "traceId":"abcd-1234",
  "msg":"Processed order event",
  "orderId": 123
}
```

--

Fichier: `/opt/backend/system-order/.github/instructions/observability.instructions.md`
