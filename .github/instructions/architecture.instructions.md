# Architecture — directives pour GitHub Copilot et l'équipe

Objectif
- Fournir une vision d'architecture centralisée pour le monorepo `system-order` afin d'aligner les décisions d'ingénierie, automatiser des suggestions IA fiables et réduire les erreurs d'intégration.

Contexte du dépôt
- Racine : `/opt/backend/system-order/pom.xml` (Maven aggregator, Java 17).
- Modules principaux : `transverse-order`, `infra-order`, `domain-order`, `infra-inventory`, `domain-inventory`, `infra-notification`, `domain-notification`, `config-server`, `eureka-server`, `gateway-server`, `order-ihm` (Angular front-end).
- Versions observées : Spring Boot `2.7.10` (modules), Spring Cloud `2021.0.8`.

Principes d'architecture
- Bounded contexts : chaque domaine métier (order, inventory, notification) est isolé en modules `domain-*` (logique métier, entités, DTOs) et `infra-*` (adapters : controllers, messaging, repositories). Respecter ce découpage.
- Clean / Hexagonal : séparer ports (interfaces de domaine) et adapters (impl). Controller -> Service (application) -> Domain -> Repository adapter.
- Autonomie des microservices : chaque service doit avoir son propre `pom.xml`, configuration, Dockerfile, tests et chart Helm.
- DTOs obligatoires : ne jamais exposer les entités JPA directement dans les APIs public.

Communication entre services
- Sync : REST via API Gateway (`gateway-server`). Versionner tous les endpoints (`/api/v1/...`).
- Async : Kafka (voir `infra-notification`) pour évènements métier. Favoriser les événements pour découpler intégration cross-domain.
- Règles pratiques :
  - Use case sync simple (lookups) → REST
  - Side effects & cross-domain changes → events (Kafka)
  - For distributed transactions, avoid two-phase commit — orchestrate via Sagas or eventual consistency events.

Résilience
- Implémenter circuit-breaker + timeout + retry (Resilience4j ou Spring Cloud Circuit Breaker) pour appels externes.
- Santé : exposer `/actuator/health`, readiness/liveness et monitorer.

Observabilité
- Traces distribuées (Spring Cloud Sleuth / OpenTelemetry) et propagation des headers traceId across REST and Kafka.
- Metrics via Micrometer → exporter Prometheus.

DDD recommandations
- Agrégats définis clairement (ex : Order aggregate contient lignes, paiements succincts).
- Eviter modèles transactionnels partagés entre services.

Anti‑patterns à éviter
- Exposer entités JPA dans controllers.
- Partager modules domain directement (couplage fort).
- Utiliser Kafka comme datastore principal.

Organisation monorepo & dépendances
- Restrictions : les modules `infra-*` peuvent dépendre sur leur `domain-*` correspondant et sur `transverse-order` pour utilitaires. Eviter dépendances circulaires.
- Tout ajout de dépendance doit être validé en regard du `pom.xml` parent et du `pom.xml` du module ciblé (chemins absolus requis dans la PR).

Exemples de structure recommandée (module `domain-order`)
- src/main/java/com/roudane/order/domain (entities)
- src/main/java/com/roudane/order/service (domain services)
- src/main/java/com/roudane/order/dto (DTOs)
- src/main/java/com/roudane/order/mapper (MapStruct)
- src/main/resources/db/migration (Flyway)

Instructions opérationnelles pour Copilot
- Toujours lire `/opt/backend/system-order/pom.xml` et le `pom.xml` du module ciblé avant de proposer des modifications.
- Toujours indiquer les chemins ABSOLUS des fichiers modifiés dans la réponse et le commit proposé.
- Inclure un plan de migration pour tout changement breaking et proposer une version d'API alternative (`/api/v2`) si nécessaire.

Contact & responsable
- Ajouter un fichier `CODEOWNERS` si non présent pour diriger les revues d'architecture.

--

Fichier: `/opt/backend/system-order/.github/instructions/architecture.instructions.md`
