# Testing — stratégie, outils et règles

But
- Garantir tests fiables, déterministes et rapides en développement; tests d'intégration robustes en CI.

Pyramide de tests
- Unit tests (JUnit5 + Mockito) — rapides, isolés.
- Integration tests (SpringBootTest + Testcontainers) — base de données, Kafka.
- End-to-end / contract tests — Gatling / Pact, environnement dédié.

Standards
- JUnit 5 obligatoire.
- Nom des tests : `ClassName_MethodUnderTest_ExpectedBehavior`.
- Tests unitaires isolés : mocker les repositories/services.
- Tests d'intégration : profil `test`, containers démarrés via Testcontainers.

Testcontainers
- Utiliser Postgres container et Kafka container pour tests d'intégration.
- Préparer scripts Flyway / Liquibase pour setup dès le start du container.

Kafka testing
- `spring-kafka-test` pour Embedded Kafka lors d'unit/integration tests si Testcontainers Kafka non disponible.
- Valider headers (traceId), schema et DLQ behavior in integration tests.

Contract testing
- Consumer-driven contract tests (Pact) pour API publiques si le projet a des consommateurs externes.

Determinisme
- Seed randoms and control clocks (use `Clock` injection) for time-sensitive tests.
- Clean DB state between tests (Flyway clean or container recreation).

CI strategy
- Unit tests run on every PR.
- Integration tests gated on nightly/merge pipeline or run with more capable CI runners.

Coverage
- Target 80% for critical modules; focus on business logic coverage over trivial getters.

Performance testing
- Separate job/environment; use Gatling/JMeter and define SLOs (p95 latency, error rate).

Fail fast
- Tests should fail early with clear messages.

Examples
```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock OrderRepository repo;
  @InjectMocks OrderServiceImpl svc;

  @Test
  void createOrder_shouldPersist_whenValid() { ... }
}
```

--

Fichier: `/opt/backend/system-order/.github/instructions/testing.instructions.md`
