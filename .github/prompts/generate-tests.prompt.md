# Generate Tests

You are a Senior QA Automation Engineer and Java Testing Specialist.

Your task is to generate complete enterprise-grade tests for the current implementation.

Before generating tests:

- Analyze the business logic
- Analyze edge cases
- Analyze security constraints
- Analyze Kafka interactions
- Analyze database behavior
- Analyze transaction boundaries

Read all files under:

.github/instructions/

---

# Testing Requirements

Generate:

- unit tests
- integration tests
- API tests
- Kafka tests
- repository tests

Use:

- JUnit 5
- Mockito
- Testcontainers
- Spring Boot Test
- MockMvc
- WireMock when needed

---

# Coverage Requirements

Cover:

- success scenarios
- validation failures
- exception handling
- security rules
- transaction rollback
- Kafka retry scenarios
- dead-letter queue scenarios
- concurrency edge cases

---

# Quality Rules

Tests must be:

- deterministic
- isolated
- maintainable
- readable
- production-grade

Avoid:

- flaky tests
- duplicated setup
- hardcoded delays
- shared mutable state

---

# Assertions

Validate:

- API responses
- database persistence
- Kafka messages
- security constraints
- business rules
- logging behavior when relevant

---

# Performance

Optimize tests for:

- fast execution
- parallel execution
- CI/CD compatibility

---

# Expected Output

Generate complete runnable test suites with:

- proper naming
- setup/teardown
- mocks
- assertions
- Testcontainers configuration
- reusable utilities