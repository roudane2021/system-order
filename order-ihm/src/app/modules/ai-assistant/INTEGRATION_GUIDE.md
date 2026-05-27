# AI Assistant Module - Integration Guide

## 🚀 Quick Start Integration

### Step 1: Module Import

```typescript
// app.module.ts
import { AiAssistantModule } from './modules/ai-assistant/ai-assistant.module';

@NgModule({
  imports: [
    AiAssistantModule,
    // ... other imports
  ]
})
export class AppModule { }
```

### Step 2: Add Route

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'ai-assistant',
    loadChildren: () => import('./modules/ai-assistant/ai-assistant.module')
      .then(m => m.AiAssistantModule)
  }
];
```

### Step 3: Update App Navigation

```typescript
// app.component.html
<nav>
  <a routerLink="/ai-assistant">AI Assistant</a>
</nav>
```

### Step 4: Install Dependencies

```bash
npm install @angular/material @angular/cdk
```

## 🔌 Backend Integration

### API Endpoints Required

```typescript
// Implement these endpoints in your backend

// 1. Send Message
POST /api/ai-assistant/chat/send
Headers: { Authorization: Bearer token }
Body: {
  conversationId: string,
  content: string,
  metadata?: {}
}
Response: {
  id: string,
  role: 'assistant',
  content: string,
  timestamp: ISO8601
}

// 2. Get Conversations
GET /api/ai-assistant/conversations
Headers: { Authorization: Bearer token }
Response: ConversationSummary[]

// 3. Get Single Conversation
GET /api/ai-assistant/conversations/:id
Headers: { Authorization: Bearer token }
Response: Conversation

// 4. Create Conversation
POST /api/ai-assistant/conversations
Headers: { Authorization: Bearer token }
Body: {
  title: string,
  systemPrompt?: string
}
Response: Conversation

// 5. Delete Conversation
DELETE /api/ai-assistant/conversations/:id
Headers: { Authorization: Bearer token }
Response: {}

// 6. Stream Response (Optional)
GET /api/ai-assistant/chat/stream?conversationId=:id
Headers: { Authorization: Bearer token }
Response: Server-Sent Events (text/event-stream)
```

### Example Backend Implementation (Spring Boot)

```java
@RestController
@RequestMapping("/api/ai-assistant")
public class AiAssistantController {

    @PostMapping("/chat/send")
    public ResponseEntity<MessageResponse> sendMessage(
        @RequestBody SendMessageRequest request,
        @RequestHeader("Authorization") String token
    ) {
        Message message = chatService.sendMessage(request);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationSummary>> getConversations() {
        List<ConversationSummary> conversations = conversationService.getUserConversations();
        return ResponseEntity.ok(conversations);
    }

    // ... other endpoints
}
```

## 🎨 Styling Integration

### Global Styles Setup

```scss
// styles.scss
@import 'app/modules/ai-assistant/styles/variables';
@import 'app/modules/ai-assistant/styles/layout';
@import 'app/modules/ai-assistant/styles/theme';
@import 'app/modules/ai-assistant/styles/responsive';
```

### Custom Theme Override

```scss
// In your custom theme file
:root {
  --primary: #your-color;
  --sidebar-bg: #your-bg;
  // ... override other variables
}
```

## 🔐 Authentication Integration

### HTTP Interceptor for Bearer Token

```typescript
// http-interceptor.service.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.auth.getToken();
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(req);
  }
}
```

### Add to App Module

```typescript
@NgModule({
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class AppModule { }
```

## 📱 Mobile Navigation Integration

### Add to Navigation Menu

```html
<!-- navigation.component.html -->
<mat-nav-list>
  <mat-list-item routerLink="/ai-assistant">
    <mat-icon matListItemIcon>smart_toy</mat-icon>
    <span matListItemTitle>AI Assistant</span>
  </mat-list-item>
</mat-nav-list>
```

## 🧪 Using Mock Service (Development)

```typescript
// chat.service.ts
constructor(
  private http: HttpClient,
  private mock: AiAssistantMockService
) {}

// For development, replace with mock
sendMessage(conversationId: string, content: string) {
  // return this.mock.sendMessage(conversationId, content);
  // Uncomment above line to use mock data
  return this.http.post(...)
}
```

## 🎯 Module Configuration

### Optional: Custom API Base URL

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  aiAssistantApiUrl: 'http://localhost:8080/api/ai-assistant'
};

// In services:
constructor(private http: HttpClient) {
  this.apiUrl = environment.aiAssistantApiUrl;
}
```

### Optional: Service Configuration

```typescript
// ai-assistant.config.ts
export const AI_ASSISTANT_CONFIG = {
  maxMessageLength: 4000,
  maxConversations: 50,
  autoScrollEnabled: true,
  typingIndicatorEnabled: true,
  soundNotificationsEnabled: false,
  theme: 'auto' // 'light' | 'dark' | 'auto'
};
```

## 🛡️ Security Considerations

1. **HTTPS Only**: Always use HTTPS in production
2. **CORS**: Configure CORS properly on backend
3. **CSRF**: Include CSRF tokens in POST requests
4. **XSS**: Angular handles XSS by default (use bypassSecurityTrustHtml carefully)
5. **Rate Limiting**: Implement rate limiting on backend
6. **Input Validation**: Validate all user inputs
7. **Authentication**: Use JWT tokens with expiration

## 📊 Analytics Integration

### Track User Actions

```typescript
// In components
this.analyticsService.trackEvent('ai_assistant_message_sent', {
  conversationId: this.conversation.id,
  messageLength: message.length
});
```

## 🚀 Performance Optimization

### Lazy Loading Already Enabled

Module is lazy-loaded by default. Check bundle size:

```bash
ng build --prod --stats-json
webpack-bundle-analyzer dist/order-ihm/stats.json
```

### Virtual Scrolling (Optional)

For large chat histories, add virtual scrolling:

```typescript
// chat-window.component.ts
import { ScrollingModule } from '@angular/cdk/scrolling';

@NgModule({
  imports: [ScrollingModule]
})
export class ChatWindowComponent { }
```

## 🧪 Testing

### Mock Service Already Provided

Use mock service for unit tests:

```typescript
// chat.service.spec.ts
beforeEach(() => {
  TestBed.configureTestingModule({
    providers: [
      { provide: ChatService, useClass: AiAssistantMockService }
    ]
  });
});
```

## 🐛 Troubleshooting

### Messages not sending
- Check API endpoint configuration
- Verify authentication token
- Check browser console for errors
- Verify CORS headers

### Sidebar not visible
- Check LayoutService state
- Verify breakpoint detection (check console)
- Check z-index and CSS classes

### Styling not applied
- Verify SCSS files are imported
- Check CSS specificity
- Clear browser cache

### Module not loading
- Verify lazy loading route
- Check module imports
- Verify component declarations

## 📚 Additional Resources

- [Component Documentation](./README.md)
- [Architecture Overview](../../docs/global/architecture-overview.md)
- [Angular Material Documentation](https://material.angular.io)

## 🤝 Support

For issues or questions:
1. Check the troubleshooting section
2. Review console errors
3. Check Angular Material docs
4. Ask your team lead

---

**Module Status:** ✅ Production Ready  
**Last Updated:** 2026-05-25  
**Angular Version:** 15+

