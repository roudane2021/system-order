# Backend — règles et conventions Java / Spring Boot

But
- Fournir des règles strictes pour l'implémentation backend afin d'assurer cohérence, testabilité et maintenance.

Contexte technique observé
- Java 17, Spring Boot modules observés : parent `spring-boot-starter-parent` v2.7.10.
- MapStruct et Lombok présents dans plusieurs modules (`infra-notification`, etc.).

Standards Java & build
- Java 17.
- Maven multi-module : exécuter `mvn -T 1C -DskipTests=false clean install` depuis la racine pour builds locaux.
- Annotation processors configurés (MapStruct, Lombok) : vérifier `pom.xml` si ajout de nouvelles libs.

Packages & organisation
- Convention recommandée (root `com.roudane.order.<context>`):
  - `api` — controllers / adapters
  - `service` — application services (orchestration)
  - `domain` — entities, value objects, domain services
  - `repository` — Spring Data JPA repositories
  - `dto` — DTOs de l'API
  - `mapper` — MapStruct mappers
  - `config` — configuration spécifique
  - `exception` — exceptions métiers et handlers

Injection & style
- Injection par constructeur ONLY (ex : `@RequiredArgsConstructor` ou explicit constructor). Pas d'injection par champ.
- Beans immutables si possible (`final` fields).
- Favoriser composition plutôt qu'héritage pour services.

Controllers
- Pas de logique métier dans controllers — controllers orchestrent les appels vers services.
- `@RestController` + `@RequestMapping("/api/v1/<resource>")`.
- Utiliser `@Validated` sur controllers et `@Valid` sur DTO inputs.
- Retourner `ResponseEntity<>` et header `Location` sur création (HTTP 201).

Services
- Contenir la logique métier et orchestration.
- Transactions : annoter méthodes de service avec `@Transactional` (si accès DB). Garder transaction scope minimal.
- Séparer application service (orchestration) des domain services (règles métier complexes).

DTOs & Mapping
- Input DTOs distincts des Output DTOs (CreateOrderDto / OrderDto).
- MapStruct pour mapping; éviter mapping manuel lourd.
- Validation via `javax.validation` (`@NotNull`, `@Size`).

Exception handling
- `@ControllerAdvice` centralisé par service pour normaliser erreurs.
- Exceptions métier → codes HTTP clairs (ex: `OrderNotFoundException` → 404).
- Ne jamais exposer stack traces en production.

Transactions & Concurrency
- Pas de transactions traversant appels réseau. Utiliser pattern Saga pour workflows multi‑services.
- Utiliser `@Version` pour optimistic locking si updates concurrents.

Logging
- SLF4J + structured logging (JSON) recommandé.
- Inclure `traceId`/`spanId` dans logs.

Asynchronous processing
- Producers publish events after DB commit (transactional outbox or `@TransactionalEventListener`).
- Consumers idempotent: dedup store (Redis/DB) keyed by eventId.

Tests
- Unit : JUnit5 + Mockito. Favoriser tests unitaires rapides.
- Integration : `@SpringBootTest` + Testcontainers pour DB/Kafka.

CI / PR expectations
- Toute modification backend requiert : tests unitaires, build module OK, JavaDoc sur méthodes publiques, mention explicite du `pom.xml` inspecté.

Exemple minimal de controller
```java
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto created = orderService.createOrder(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
```

--

Fichier: `/opt/backend/system-order/.github/instructions/backend.instructions.md`
