# Sécurité — directives pratiques et interdits

But
- Standardiser l'approche sécurité pour Gateway, microservices, Kafka et Kubernetes. Fournir règles claires pour Copilot afin d'éviter pratiques dangereuses.

Architecture d'authentification
- Gateway (`gateway-server`) : point d'entrée unique pour l'authentification et l'autorisation.
  - Valider JWT via JWKS (`jwk-set-uri`) au niveau de la Gateway.
  - Propager headers de confiance (après validation) aux microservices si nécessaire.
- Microservices : configuration `spring-boot-starter-oauth2-resource-server` pour vérifier tokens côté ressource.

JWT & OAuth2
- JWT claims à valider systématiquement : `iss`, `aud`, `exp`, `iat`, `sub`, scopes/roles.
- Durées courtes tokens d'accès, refresh tokens gérés par l'Auth server.

RBAC
- Utiliser roles/authorities et `@PreAuthorize` ou `MethodSecurity` dans services.
- Définir roles standardisés (`ORDER_ADMIN`, `ORDER_USER`, `INVENTORY_ADMIN`) et documenter dans central README.

Secrets management
- Ne jamais commettre secrets. Utiliser :
  - GitHub Secrets pour CI
  - HashiCorp Vault / AWS Secrets Manager pour production
  - Kubernetes Secrets (s'il est chiffré via SealedSecrets / External Secrets)
- Dans `application.yml`, utiliser placeholders `${...}` et documenter variables d'environnement requises.

Secure headers & hardening
- Gateway doit appliquer headers : `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, CSP minimal.
- Désactiver endpoints sensibles d'Actuator en prod ou les protéger via RBAC.

Input validation & OWASP
- Validation DTOs (`@Valid`) + server-side checks.
- Sanitize inputs that seront réinjectés dans DB ou shell.
- Use parameterized queries / JPA Criteria / PreparedStatement to prevent SQLi.

Kafka security
- Production: TLS + SASL (SCRAM/OAUTHBEARER) or mTLS. Configure brokers via Helm charts (see `infra-helm/cluster-kafka`).
- Do not store secrets in topic messages. PII must be masked or tokenized.

Kubernetes
- Use RBAC and Namespace separation (`dev`, `staging`, `prod`).
- Use external secret stores or sealed secrets; never put secrets in values.yaml.
- Restrict pod security (no privileged containers) and drop capabilities.

Forbidden practices (ne jamais faire)
- Commit credentials or sample tokens with real values.
- Disable TLS for production Kafka or HTTP in prod.
- Rely solely on client-supplied headers for authorization.

Secure coding examples
- Validate JWT in Gateway config example (application.yml)
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://auth.example.com/.well-known/jwks.json
```
- Example of method security
```java
@PreAuthorize("hasRole('ORDER_ADMIN') or hasAuthority('orders:update')")
public void updateOrder(...) { ... }
```

Audit & rotation
- Rotate secrets periodically and ensure revocation procedure.
- Log security events centrally (failed logins, token validation failures) and alert.

Checklist de sécurité pour PR
- [ ] Pas de secrets introduits
- [ ] Configuration JWT/JWKS validée
- [ ] Tests d'autorisation ajoutés
- [ ] Documentation des variables d'environnement

--

Fichier: `/opt/backend/system-order/.github/instructions/security.instructions.md`
