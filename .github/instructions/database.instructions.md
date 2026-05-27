# Database — conventions, migrations et bonnes pratiques

But
- Standardiser la conception de bases, migrations et l'utilisation de JPA/Hibernate pour assurer performance et maintenabilité.

Choix & ownership
- Chaque microservice possède sa base/schema indépendante (bounded context). Eviter bases partagées pour plusieurs services sauf pour read-only shared datasets.
- Préférence production : PostgreSQL. MySQL acceptable avec documentation des différences.

Nommage
- Tables & colonnes : snake_case.
- Tables : nom clair, pluriel ou singulier homogène dans le projet (ex: `orders`, `order_items`).
- PK : `id` (bigint / UUID selon besoin) ; FK suffix `_id`.
- Columns audit: `created_at`, `created_by`, `updated_at`, `updated_by`, `deleted_at`.

Migrations
- Flyway recommandé. Scripts sous `src/main/resources/db/migration`.
- Nommage : `V{major}__{short_description}.sql` (ex: `V1__create_orders_table.sql`).
- Les migrations doivent être testées en CI (staging run) et idempotentes autant que possible.

Indexation & requêtes
- Indexer colonnes utilisées en WHERE / JOIN / ORDER BY.
- Eviter indexes trop larges ; privilégier composite indexes ciblés.
- Préférer projections et pagination côté DB pour listes volumineuses.

JPA & ORM
- Ne pas exposer entités JPA dans l'API.
- Eviter `@Data` sur entités ; préférer `@Getter`/`@Setter` et implémenter `equals`/`hashCode` prudemment.
- Gestion de N+1 : utiliser fetch joins ou DTO projections.
- Utiliser `@Version` pour optimistic locking sur entités souvent modifiées.

Soft delete & audit
- Implémenter soft delete via `deleted_at` timestamp et filtres globaux si nécessaire (avec prudence).
- Auditing : utiliser `@EntityListeners` ou triggers DB pour historisation.

Performance
- Revue SQL lente via `EXPLAIN ANALYZE` et création d'indexes ciblés.
- Archiver données anciennes pour réduire taille des tables critiques.

Transactions
- Utiliser `@Transactional` au niveau service; éviter transactions longues et couvrant appels réseaux.

Anti‑patterns interdits
- Utiliser ORM pour queries complexes non optimisées sans profiler.
- Multiples cascade remove non contrôlés sur entités volumineuses.

Testing DB
- Tests d'intégration : Testcontainers Postgres, exécuter migrations à setup.

--

Fichier: `/opt/backend/system-order/.github/instructions/database.instructions.md`
