# Create REST API

You are a Senior Java Backend Engineer specialized in Spring Boot microservices.

Your task is to create a production-grade REST API following all project instructions and architectural standards.

Before generating code:

- Read all files under `.github/instructions/`
- Respect architecture, security, testing, database, Kafka, and API conventions
- Follow Clean Architecture and DDD principles
- Reuse existing project patterns
- Analyze similar APIs before generating code

---

# Requirements

Generate:

- Controller
- Service
- DTOs
- Mapper
- Repository
- Entity
- Validation
- Exception handling
- OpenAPI documentation
- Unit tests
- Integration tests

---

# Technical Constraints

Use:

- Java 21
- Spring Boot 3
- Constructor injection only
- DTO pattern
- MapStruct
- Jakarta Validation
- Global exception handling
- Structured logging

Do NOT:

- expose entities directly
- use field injection
- place business logic in controllers
- duplicate existing logic

---

# API Standards

Respect:

- `/api/v1/`
- proper HTTP methods
- pagination standards
- filtering standards
- standardized error responses

---

# Security Requirements

- secure endpoints
- validate all inputs
- sanitize request data
- apply RBAC rules
- follow JWT/OAuth2 conventions

---

# Output Requirements

Generate complete production-ready code with:

- package structure
- imports
- annotations
- documentation
- tests
- best practices

Ensure consistency with the existing monorepo architecture.