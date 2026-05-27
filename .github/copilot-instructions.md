# Guide de référence pour GitHub Copilot — projet multi‑modules microservices Java

Tâche reçue — Je crée le fichier `.github/copilot-instructions.md` qui servira de "cerveau" et de guide opérationnel pour toutes les futures interactions automatisées (Copilot, assistants IA) avec ce dépôt.

Plan d'action bref :
1. Fournir une checklist à exécuter avant toute modification.
2. Documenter la vue d'ensemble du repo et la stack technique.
3. Définir l'architecture attendue, les conventions de code et d'API.
4. Spécifier les règles de sécurité, tests et CI.
5. Donner des méta‑règles strictes que Copilot doit suivre (vérification de `pom.xml`, chemins absolus, JavaDoc, etc.).

---

## Checklist initiale (obligatoire avant toute suggestion ou modification)

- [ ] Ouvrir et lire le `pom.xml` parent : `/opt/backend/system-order/pom.xml`.
- [ ] Ouvrir et lire le `pom.xml` du module ciblé (ex. `/opt/backend/system-order/domain-order/pom.xml`).
- [ ] Vérifier les workflows GitHub existants : `/opt/backend/system-order/.github/workflows/`.
- [ ] Vérifier l'existence de `CODEOWNERS`, `LICENSE`, `CONTRIBUTING.md` à la racine ou dans `/.github/`.
- [ ] Lister les fichiers à modifier et toujours communiquer le chemin absolu (ex. `/opt/backend/system-order/gateway-server/src/main/java/...`).
- [ ] Ajouter JavaDoc pour toutes les nouvelles méthodes publiques.
- [ ] Écrire ou mettre à jour les tests unitaires et d'intégration avant d'ouvrir une PR.
- [ ] Ne jamais proposer ni insérer de secrets en clair (tokens, clés, mots de passe).

---

# 1. Vue d'ensemble & Stack technique

## Objectif du dépôt
Ce dépôt contient une architecture microservices multi‑module Java (Maven) avec :
- un Config Server (`config-server/`),
- un Service Registry/Eureka (`eureka-server/`),
- un API Gateway (`gateway-server/`),
- plusieurs microservices métiers (`domain-order/`, `domain-inventory/`, `domain-notification/`, ...),
- une UI Angular (`order-ihm/`),
- chartes Helm et fichiers d'infrastructure (`infra-helm/`).

## Stack technique (valider les versions dans les `pom.xml`)
- Java 17+ (ou version spécifiée dans `pom.xml`),
- Spring Boot (version définie dans `pom.xml`),
- Spring Cloud (Eureka, Config, Gateway),
- Maven multi‑module,
- Lombok, MapStruct (si présent),
- Spring Data JPA / Hibernate,
- Micrometer, Sleuth / OpenTelemetry,
- JUnit 5, Mockito, Testcontainers (pour tests d'intégration),
- Angular (frontend `order-ihm/`) — vérifier `package.json` et `angular.json`.

---

# 2. Architecture & Conception

## Principes généraux
- Respecter la séparation des responsabilités : Controller (API) → Service (logique métier) → Repository (persistence).
- Favoriser l'architecture hexagonale / Clean Architecture : utiliser ports & adapters lorsque pertinent.
- Ne jamais exposer les entités JPA directement dans l'API : utiliser des DTOs.
- Chaque microservice est autonome : code, tests, configuration, `pom.xml` propres.

## Structure de package recommandée (ex. `com.company.order`)
- `com.company.order.api` — contrôleurs REST
- `com.company.order.service` — logique métier
- `com.company.order.domain` — entités/domain objects
- `com.company.order.dto` — DTOs pour l'API
- `com.company.order.repository` — repositories Spring Data
- `com.company.order.config` — configurations Spring
- `com.company.order.exception` — exceptions custom et handler
- `com.company.order.mapper` — MapStruct mappers

---

# 3. Conventions de nommage et style de code

## Java / Spring Boot
- Classes : `UpperCamelCase` (ex. `OrderService`).
- Interfaces : `UpperCamelCase` (ex. `OrderRepository`).
- Packages : en minuscules, par domaine (ex. `com.company.domain.order`).
- Variables / méthodes : `lowerCamelCase`.
- Constantes : `UPPER_SNAKE_CASE`.
- Beans Spring : nom par défaut (lowerCamel de la classe) sauf exceptions.
- DTOs : suffixe `Dto` (ex. `OrderDto`, `CreateOrderDto`).
- Tests unitaires : suffixe `*Test`; tests d'intégration : `*IT` / `*IntegrationTest`.

## Lombok
- Autorisé mais prudent :
  - `@RequiredArgsConstructor` pour injection par constructeur.
  - `@Getter`, `@Setter`, `@Builder` selon besoin.
  - Éviter `@Data` sur des entités JPA pour prévenir problèmes equals/hashCode.

## JavaDoc
- Toute méthode publique doit avoir un JavaDoc succinct décrivant : but, paramètres, valeur de retour, exceptions lancées.

Exemple :
```java
/**
 * Récupère la commande par identifiant.
 *
 * @param id identifiant de la commande
 * @return DTO représentant la commande
 * @throws OrderNotFoundException si la commande n'existe pas
 */
public OrderDto findById(Long id) { ... }
```

---

# 4. Règles pour les API REST

## Endpoints & versioning
- Versionner via chemin : `/api/v1/...`.
- Noms de ressources au pluriel : `/api/v1/orders`, `/api/v1/customers`.
- Conventions HTTP :
  - GET /orders — liste (avec pagination et filtrage),
  - GET /orders/{id} — détail,
  - POST /orders — création,
  - PUT /orders/{id} — remplacement,
  - PATCH /orders/{id} — mise à jour partielle,
  - DELETE /orders/{id} — suppression.

## Validation et DTOs
- Utiliser `@Valid` et contraintes `javax.validation` sur DTOs (`@NotNull`, `@Size`, ...).
- Utiliser MapStruct pour mapper DTO ↔ Entity.

## Gestion centralisée des erreurs
- Utiliser un `@ControllerAdvice` unique par service pour normaliser les réponses d'erreur.
- Exemple de format d'erreur :
```json
{
  "timestamp": "2026-05-27T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found",
  "path": "/api/v1/orders/123"
}
```

## Pagination & tri
- Utiliser `Pageable` et `Page<T>` de Spring Data.
- Paramètres standard : `page`, `size`, `sort`.

---

# 5. Sécurité

## Principes
- Gateway = point d'entrée unique pour l'authentification.
- Gateway valide le token (JWT / OAuth2) ; les microservices valident les tokens côté resource-server.
- Communication service-to-service : utiliser mTLS ou tokens de service.
- Ne pas faire confiance aux headers non signés fournis par le client.

## Exemples
- Gateway `application.yml` :
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://auth.example.com/.well-known/jwks.json
```
- Microservices : configurer `spring-boot-starter-oauth2-resource-server` pour valider les JWT.
- Autorisations : utiliser `@PreAuthorize` pour contrôler l'accès basé sur rôles/permissions.

---

# 6. Conventions de tests

## Tests unitaires
- JUnit 5 + Mockito.
- Mocker les repositories/services dans tests unitaires.
- Noms : `ClassName_MethodUnderTest_ExpectedBehavior`.

Exemple :
```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private OrderRepository repo;
    @InjectMocks private OrderServiceImpl service;

    @Test
    void createOrder_shouldPersist_whenValid() { ... }
}
```

## Tests d'intégration
- `@SpringBootTest` avec profil `test`.
- Préférer Testcontainers pour DB et services externes.
- Vérifier endpoints via `MockMvc` ou `TestRestTemplate`.

---

# 7. Observabilité & monitoring

- Micrometer + Prometheus.
- Traces distribués : Sleuth ou OpenTelemetry.
- Logs structurés (JSON) et propagation de `traceId` / `spanId`.
- Actuator exposé sous contrôle d'accès (`/actuator/health`, `/actuator/prometheus`).

---

# 8. CI / CD & commandes utiles

## Build multi‑module (depuis la racine)
```bash
mvn -T 1C clean install
# ou pour un module précis
mvn -f /opt/backend/system-order/domain-order/pom.xml clean install
```

## Lancer tests d'un module
```bash
mvn -f /opt/backend/system-order/domain-order/pom.xml test
```

## Afficher les dépendances d'un module
```bash
mvn -f /opt/backend/system-order/domain-order/pom.xml dependency:tree
```

## Angular (frontend)
```bash
# depuis order-ihm/
npm install
npm run build
ng serve --open
```

---

# 9. Méta‑règles (instructions impératives pour Copilot)

Ces règles sont obligatoires — toute suggestion doit les respecter.

1. Vérifier les `pom.xml` avant de proposer l'ajout d'une dépendance ou une solution technique :
   - Ouvrir `/opt/backend/system-order/pom.xml` et le `pom.xml` du module ciblé.
   - Toujours indiquer les versions trouvées et la justification de la version proposée.
   - Exemple de commande (à suggérer mais ne pas exécuter automatiquement) :
   ```bash
   mvn -q -f /opt/backend/system-order/<module>/pom.xml dependency:tree
   ```

2. Toujours préciser le chemin ABSOLU du fichier modifié dans la réponse et dans le commit proposé.
   - Exemple : "Fichier modifié : `/opt/backend/system-order/gateway-server/src/main/java/com/company/gateway/SecurityConfig.java`".

3. JavaDoc obligatoire pour toute méthode publique ajoutée ou modifiée.
   - Inclure `@param`, `@return` et `@throws` lorsque pertinent.

4. Tests obligatoires :
   - Toute logique métier nouvelle doit être accompagnée d'un test unitaire.
   - Toute API publique nouvelle doit être accompagnée d'un test d'intégration minimal.

5. Respect strict du style existant : indentation, ordre des imports, limites de lignes si le projet l'exige.

6. Ne jamais injecter de secrets dans le code : utiliser des placeholders et indiquer comment configurer via variables d'environnement, Vault ou secret manager.

7. Compatibilité ascendante : ne pas casser les endpoints publics ; versionner si changement non rétrocompatible.

8. Pour des modifications front‑end (`order-ihm/`) : préciser la version Angular et le/les fichiers modifiés, ex :
   - `Fichier modifié : /opt/backend/system-order/order-ihm/src/app/modules/order-management/orders/order-create/order-create.component.ts`.

9. Pour chaque PR proposée, inclure une checklist dans la description :
   - Build local OK, Tests locaux OK,
   - Chemins des fichiers modifiés,
   - JavaDoc ajoutée,
   - Vérification `pom.xml` faite.

10. En cas d’échec de build dans l’environnement d’intégration, retourner l’erreur complète et proposer un plan de correction minimal et sûr.

---

# 10. Modèle de message à utiliser pour suggestions de modification (template)

Quand Copilot propose un changement, utiliser ce modèle pour la réponse :

- Résumé court du changement.
- Fichiers modifiés (chemins absolus).
- Contexte (pourquoi le changement est nécessaire).
- Diff/patch proposé (ou commandes pour appliquer).
- Tests ajoutés / mis à jour (chemins).
- Commandes pour vérifier localement (build/tests).
- Impact/risques et notes de compatibilité.

Exemple :
```
Résumé : Ajout d'un endpoint POST /api/v1/orders pour créer une commande.
Fichiers modifiés :
 - /opt/backend/system-order/domain-order/src/main/java/com/company/order/api/OrderController.java
 - /opt/backend/system-order/domain-order/src/main/java/com/company/order/service/OrderService.java
 - /opt/backend/system-order/domain-order/src/test/java/com/company/order/OrderControllerTest.java
Contexte : Permettre la création de commandes depuis la nouvelle UI.
Commandes de vérification : mvn -f /opt/backend/system-order/domain-order/pom.xml test
Impact : API v1, compatibilité OK.
```

---

# 11. Exemples techniques courts (snippets)

## Controller minimal (exemple)
```java
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * Crée une commande.
     */
    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto created = orderService.createOrder(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
```

## GlobalExceptionHandler (exemple)
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(OrderNotFoundException ex, HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
}
```

---

# 12. Processus PR / revue

Avant d'ouvrir la PR :
- Exécuter `mvn -T 1C clean install` depuis la racine (ou `mvn -f <module>/pom.xml install` pour un module ciblé).
- Vérifier tests `mvn test`.
- Lister fichiers modifiés avec chemins absolus dans la description de la PR.
- Joindre la checklist (voir début du fichier).

Reviewer checklist :
- [ ] Build local OK
- [ ] Tests unitaires OK
- [ ] Tests d'intégration OK
- [ ] JavaDoc pour méthodes publiques
- [ ] Pas de secrets
- [ ] Respect des `pom.xml` / versions

---

# 13. Contacts & ownership

- Si un fichier `CODEOWNERS` existe, s'y référer pour assigner automatiquement les reviewers.
- En l'absence de `CODEOWNERS`, mentionner les lead‑devs dans la PR (consulter `README.md` ou `CONTRIBUTING.md`).

---

# 14. Historique et mises à jour de ce guide

- Ce fichier sera maintenu à jour par l'équipe plateforme et les mainteneurs du repo.
- Toute modification majeure doit suivre la procédure PR et être revue par `CODEOWNERS`.

---

# Remarques finales (obligatoire)

- Copilot / IA : respecter strictement les "Méta‑règles" de la section 9 ; en cas d'incertitude sur une décision technique majeure, proposer une ou deux options avec leurs implications et demander validation humaine.
- Place du fichier : `/opt/backend/system-order/.github/copilot-instructions.md`.


---

*Fichier généré le 2026-05-27.*

