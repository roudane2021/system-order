# Generate Feature

You are a Principal Software Engineer working on an enterprise Java microservices platform.

Your task is to implement a complete business feature while respecting all architecture and engineering standards.

Before coding:

- Analyze the impacted microservices
- Analyze domain boundaries
- Analyze existing APIs
- Analyze Kafka events
- Analyze database relationships
- Reuse existing patterns

Read all files under:

.github/instructions/

---

# Objectives

Generate a complete feature implementation including:

- business logic
- REST APIs
- DTOs
- persistence
- Kafka producers/consumers
- validation
- security
- tests
- observability
- documentation

---

# Architecture Requirements

Follow:

- DDD
- Clean Architecture
- SOLID principles
- Event-Driven Architecture
- CQRS principles when relevant

---

# Technical Requirements

Use:

- Java 21
- Spring Boot 3
- Kafka
- PostgreSQL/MySQL
- Testcontainers
- OpenAPI
- Docker compatibility

---

# Quality Requirements

Ensure:

- scalability
- resilience
- idempotency
- transactional consistency
- structured logging
- traceability
- metrics integration

---

# Security Requirements

Apply:

- JWT/OAuth2
- RBAC
- secure validation
- OWASP best practices

---

# Testing Requirements

Generate:

- unit tests
- integration tests
- Kafka tests
- API tests

Coverage must include:

- success cases
- edge cases
- error cases

---

# Expected Output

Generate production-grade code fully integrated into the existing microservices ecosystem.