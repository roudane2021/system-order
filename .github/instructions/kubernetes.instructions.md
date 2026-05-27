# Kubernetes & Helm — production hardening et conventions

But
- Décrire standards de déploiement, sécurité, ressources et observabilité pour les charts Helm et manifests.

Namespaces & RBAC
- Séparer environnements : `dev`, `staging`, `prod`.
- Appliquer RBAC finement ; restreindre accès à `prod`.

Helm structure & values
- Un chart par service ou chart umbrella avec subcharts. Placer charts dans `infra-helm/` et `order-system-helm/`.
- Values files par environment : `values-dev.yaml`, `values-staging.yaml`, `values-prod.yaml`.
- Exclure secrets des values files ; utiliser ExternalSecrets / SealedSecrets.

Probes & resources
- Liveness probe : `/actuator/health/liveness`.
- Readiness probe : `/actuator/health/readiness`.
- Define resource requests & limits basés on profiling.

Autoscaling
- Configure HPA with CPU and custom metrics (latency, queue length).
- Define min/max replicas appropriate to SLA.

Rolling updates & deployment
- Rolling update strategy with `maxUnavailable=0`, `maxSurge=1` for critical services.
- Use canary strategy for major releases (subset of traffic).

Network & security
- NetworkPolicies to limit cross-namespace traffic.
- PodSecurity admission (no privileged, drop capabilities).
- Image scanning in CI (Trivy/Snyk) and supply SBOM when possible.

Config & secrets
- Use ConfigMaps for non-sensitive configuration and Secrets (K8s Secret / Vault) for secrets.
- Mount secrets as env or files and avoid printing their values in logs.

Observability integration
- ServiceMonitor / Pod annotations for Prometheus scraping.
- Fluentd/FluentBit sidecar or cluster-level log collector.

CI/CD integration
- GitHub Actions builds images (`.github/workflows/docker-image.yml`).
- Deploy via Helm in CD pipeline (external tool or ArgoCD/Flux).

Example values snippet
```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 15
resources:
  requests:
    cpu: 250m
    memory: 512Mi
  limits:
    cpu: 1
    memory: 1Gi
```

--

Fichier: `/opt/backend/system-order/.github/instructions/kubernetes.instructions.md`
