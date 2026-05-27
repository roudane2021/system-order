# API — standards REST, contrats et bonnes pratiques

But
- Standardiser la conception des API REST exposées par les microservices et fournir des conventions claires pour Copilot et les développeurs.

Versioning & routes
- Version via path : `/api/v1/...`. Breaking changes → `/api/v2/...`.
- Resources en pluriel : `/api/v1/orders`, `/api/v1/customers`.

HTTP status codes
- 200 OK — GET successful
- 201 Created — POST created (Location header)
- 204 No Content — DELETE success
- 400 Bad Request — validation errors
- 401 Unauthorized — authentication required
- 403 Forbidden — insufficient permission
- 404 Not Found — resource absent
- 409 Conflict — business rule violation
- 500 Internal Server Error — unexpected errors

Request/Response patterns
- Use DTOs for requests and responses (CreateOrderDto, OrderDto).
- Pagination : `page`, `size`, `sort` query params; response envelope containing `totalElements`, `totalPages`, `content`.
- Search/Filter : lightweight filters in query params; complex filters via POST `/orders/search` with search DTO.

Error envelope (standard)
```json
{
  "timestamp":"2026-05-27T12:00:00Z",
  "status":400,
  "error":"Bad Request",
  "message":"Validation failed",
  "path":"/api/v1/orders"
}
```

Validation
- Use `@Valid` and javax validation annotations on DTOs.
- Centralize validation errors formatting in `@ControllerAdvice`.

OpenAPI & documentation
- Publish OpenAPI docs (`/v3/api-docs`) and Swagger UI in non-production or protected in production.
- Keep models annotated with descriptions for consumers.

Rate limiting & throttling
- Implement at Gateway level per consumer API key or per user to protect backend.

Caching
- Cache idempotent read responses at the Gateway layer with short TTL when applicable.

Anti‑patterns
- Returning JPA entities directly.
- Using verbs in resource names.
- Overloading a single endpoint with multiple unrelated responsibilities.

Best practice example
```java
@GetMapping
public ResponseEntity<Page<OrderDto>> list(Pageable pageable, @RequestParam Optional<String> status) {
  Page<OrderDto> page = orderService.findAll(pageable, status);
  return ResponseEntity.ok(page);
}
```

--

Fichier: `/opt/backend/system-order/.github/instructions/api.instructions.md`
