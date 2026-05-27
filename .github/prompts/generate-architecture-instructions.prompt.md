# Enterprise Architecture Analysis & Instruction Generation

You are a Principal Software Architect and Senior Staff Engineer.

Your mission is to analyze the entire repository and generate a complete AI-driven engineering governance structure for GitHub Copilot.

You must deeply inspect:

- project structure
- Maven modules
- Spring Boot services
- package organization
- dependencies
- Kafka usage
- REST APIs
- security implementation
- Docker/Kubernetes configuration
- testing strategy
- observability stack
- CI/CD workflows
- coding conventions
- architectural patterns

The project is a Java Spring Boot microservices platform.

Architecture includes:
- API Gateway
- Config Server
- Eureka Discovery Server
- Multiple Microservices
- Kafka Event-Driven Communication
- PostgreSQL/MySQL
- Docker
- Kubernetes

---

# Your Responsibilities

Act as:

- Enterprise Architect
- Solution Architect
- Security Architect
- Platform Engineer
- Backend Tech Lead
- DevOps Architect
- Observability Engineer

---

# Objective

Generate a complete `.github/instructions/` folder containing high-quality engineering governance documentation for GitHub Copilot.

You must create the following files:

- architecture.instructions.md
- backend.instructions.md
- security.instructions.md
- kafka.instructions.md
- database.instructions.md
- testing.instructions.md
- observability.instructions.md
- kubernetes.instructions.md
- api.instructions.md

---

# Global Requirements

All generated instruction files must:

- be production-grade
- be enterprise-ready
- be optimized for large teams
- follow clean architecture principles
- enforce consistency across microservices
- reduce AI hallucinations
- improve Copilot code generation quality
- contain concrete examples
- contain rules and anti-patterns
- contain naming conventions
- contain recommended package structures
- contain code quality standards
- contain scalability recommendations
- contain security best practices
- contain performance recommendations

---

# Analysis Requirements

Before generating instructions:

1. Analyze all Maven modules
2. Detect all microservices
3. Detect shared libraries
4. Detect architectural patterns
5. Detect communication patterns
6. Detect Kafka topics and consumers
7. Detect security implementation
8. Detect DTO/entity separation
9. Detect persistence strategy
10. Detect testing framework
11. Detect infrastructure configuration
12. Detect deployment strategy
13. Detect API conventions
14. Detect logging and tracing strategy

---

# architecture.instructions.md

This file must define:

- overall architecture principles
- microservices responsibilities
- bounded contexts
- service communication strategy
- sync vs async communication rules
- event-driven architecture rules
- DDD recommendations
- clean architecture recommendations
- resilience patterns
- scalability recommendations
- anti-patterns to avoid
- monorepo organization strategy

Include:
- examples
- folder structures
- naming conventions
- dependency rules

---

# backend.instructions.md

This file must define:

- Java standards
- Spring Boot conventions
- package structure
- service layer rules
- controller rules
- DTO rules
- mapper rules
- exception handling
- validation strategy
- transaction management
- async processing rules
- dependency injection standards
- logging conventions

Enforce:
- constructor injection only
- no field injection
- no business logic in controllers
- DTO-only API exposure

---

# security.instructions.md

This file must define:

- authentication strategy
- authorization rules
- JWT standards
- OAuth2 recommendations
- RBAC rules
- API protection
- secret management
- secure headers
- OWASP recommendations
- input validation
- SQL injection prevention
- Kafka security
- Kubernetes secret handling

Include:
- forbidden practices
- secure coding examples
- token handling rules

---

# kafka.instructions.md

This file must define:

- topic naming conventions
- event versioning
- retry strategies
- dead-letter queue strategy
- consumer group naming
- schema evolution
- idempotency rules
- event payload standards
- producer/consumer responsibilities
- observability for Kafka

Include:
- naming examples
- anti-patterns
- resiliency recommendations

---

# database.instructions.md

This file must define:

- database naming standards
- table naming conventions
- migration strategy
- Flyway/Liquibase rules
- indexing strategy
- transaction boundaries
- performance optimization
- JPA/Hibernate best practices
- entity design rules
- soft delete strategy
- audit strategy

Include:
- forbidden ORM practices
- query optimization recommendations

---

# testing.instructions.md

This file must define:

- testing pyramid
- unit testing strategy
- integration testing strategy
- Testcontainers usage
- mocking strategy
- contract testing
- performance testing
- Kafka testing
- API testing
- coverage requirements

Enforce:
- JUnit 5
- deterministic tests
- isolated tests

---

# observability.instructions.md

This file must define:

- structured logging
- correlation IDs
- distributed tracing
- metrics standards
- Prometheus recommendations
- Grafana recommendations
- OpenTelemetry strategy
- alerting recommendations
- dashboard strategy

Include:
- log format examples
- tracing examples

---

# kubernetes.instructions.md

This file must define:

- deployment standards
- Helm conventions
- namespace strategy
- ingress strategy
- autoscaling
- resource limits
- probes configuration
- secrets management
- config maps strategy
- rolling deployment strategy
- observability integration

Include:
- production hardening recommendations

---

# api.instructions.md

This file must define:

- REST API standards
- endpoint naming
- API versioning
- pagination standards
- filtering standards
- error response format
- HTTP status code usage
- OpenAPI standards
- validation standards
- response envelope standards

Include:
- JSON examples
- naming examples
- anti-patterns

---

# Expected Output

Generate:

1. All instruction files
2. Production-grade markdown
3. Well-structured sections
4. Clear actionable rules
5. Enterprise architecture guidance
6. AI-optimized instructions for GitHub Copilot

The generated files must be directly usable inside:

.github/instructions/

and must improve consistency, maintainability, scalability, and AI-assisted development quality across the entire platform.