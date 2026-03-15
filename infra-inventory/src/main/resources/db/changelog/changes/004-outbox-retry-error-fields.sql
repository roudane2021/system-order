ALTER TABLE outbox ADD (
    retry_count NUMBER DEFAULT 0 NOT NULL,
    last_attempt_at TIMESTAMP NULL,
    error_message VARCHAR2(2000),
    error_stacktrace CLOB
);
