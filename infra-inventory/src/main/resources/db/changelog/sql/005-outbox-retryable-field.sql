ALTER TABLE outbox ADD (
    retryable NUMBER(1) DEFAULT 1 NOT NULL
);
