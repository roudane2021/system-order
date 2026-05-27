# AI Assistant Module - Modern Angular Chat Interface

Un module Angular complet et production-ready pour une interface de chat moderne inspirée de ChatGPT.

## 📋 Table des matières

- [Caractéristiques](#caractéristiques)
- [Installation](#installation)
- [Structure](#structure)
- [Utilisation](#utilisation)
- [Configuration](#configuration)
- [Components](#components)
- [Services](#services)
- [Styling](#styling)

## ✨ Caractéristiques

### UI/UX
- ✅ Interface similaire à ChatGPT
- ✅ Sidebar avec historique des conversations
- ✅ Chat window avec scroll automatique
- ✅ Textarea auto-resize
- ✅ Typing animation
- ✅ Message grouping
- ✅ Responsive design (mobile, tablet, desktop)

### Architecture
- ✅ Standalone components (Angular 15+)
- ✅ Services RxJS
- ✅ Angular Material
- ✅ Modular SCSS
- ✅ Clean code principles
- ✅ Production-grade

### Features
- ✅ Conversation management
- ✅ Message history
- ✅ Dark/Light theme
- ✅ Keyboard shortcuts (Ctrl+Enter to send)
- ✅ Message actions (copy)
- ✅ Conversation export
- ✅ Loading states
- ✅ Error handling

## 📦 Installation

### 1. Importer le module

```typescript
// app.module.ts
import { AiAssistantModule } from './modules/ai-assistant/ai-assistant.module';

@NgModule({
  imports: [
    // ... autres imports
    AiAssistantModule
  ]
})
export class AppModule { }
```

### 2. Ajouter la route

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'ai',
    loadChildren: () => import('./modules/ai-assistant/ai-assistant.module')
      .then(m => m.AiAssistantModule)
  }
];
```

### 3. Importer les styles globaux

```scss
// styles.scss
@import 'app/modules/ai-assistant/styles/variables';
@import 'app/modules/ai-assistant/styles/layout';
@import 'app/modules/ai-assistant/styles/theme';
@import 'app/modules/ai-assistant/styles/responsive';
```

### 4. Configurer Angular Material

```typescript
// material.module.ts
import { NgModule } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
// ... autres imports

@NgModule({
  exports: [
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    // ...
  ]
})
export class MaterialModule { }
```

## 🏗️ Structure

```
ai-assistant/
├── components/
│   ├── sidebar/                    # Chat history sidebar
│   ├── topbar/                     # Header with controls
│   ├── chat-window/                # Main message area
│   ├── message-item/               # Single message component
│   ├── prompt-input/               # Input textarea
│   └── typing-indicator/           # Typing animation
├── pages/
│   └── assistant-page/             # Main page component
├── services/
│   ├── chat.service.ts             # Message handling
│   ├── conversation.service.ts     # Conversation management
│   └── layout.service.ts           # Layout state
├── models/
│   ├── message.model.ts            # Message interfaces
│   └── conversation.model.ts       # Conversation interfaces
├── styles/
│   ├── _variables.scss             # Theme variables
│   ├── _layout.scss                # Layout styles
│   ├── _theme.scss                 # Theme configuration
│   └── _responsive.scss            # Responsive styles
├── ai-assistant.module.ts          # Main module
└── ai-assistant-routing.module.ts  # Routing
```

## 🚀 Utilisation

### Accéder au module

```
http://localhost:4200/ai-assistant
```

### Envoyer un message

```typescript
// Automatically handled by PromptInputComponent
// User types message and presses Ctrl+Enter or clicks Send
```

### Créer une nouvelle conversation

```typescript
this.conversationService.createConversation({
  title: 'New Chat'
}).subscribe(conversation => {
  console.log('Created:', conversation);
});
```

### Charger une conversation

```typescript
this.conversationService.loadConversation(conversationId).subscribe(
  conversation => {
    console.log('Loaded:', conversation);
  }
);
```

## ⚙️ Configuration

### Backend API Endpoints

Configurez ces endpoints dans `ChatService` et `ConversationService`:

```typescript
// Send message
POST /api/ai-assistant/chat/send
Body: { conversationId, content }

// Get conversations
GET /api/ai-assistant/conversations

// Get single conversation
GET /api/ai-assistant/conversations/:id

// Create conversation
POST /api/ai-assistant/conversations
Body: { title, systemPrompt }

// Delete conversation
DELETE /api/ai-assistant/conversations/:id

// Stream messages
GET /api/ai-assistant/chat/stream?conversationId=:id
```

### Theme Customization

Modifiez les variables dans `styles/_variables.scss`:

```scss
:root {
  --primary: #1976d2;
  --primary-text: #212121;
  --sidebar-bg: #f5f5f5;
  // ... etc
}
```

### Keyboard Shortcuts

- **Ctrl+Enter** - Send message
- **Shift+Enter** - New line in input
- **Escape** - Close sidebar (mobile)

## 🔧 Components

### SidebarComponent
- Chat history list
- New chat button
- User profile section
- Delete conversation option

### TopbarComponent
- Conversation title
- Menu button
- Share option
- Export conversation
- Settings menu

### ChatWindowComponent
- Message display
- Auto-scroll
- Empty state
- Typing indicator
- Message grouping

### PromptInputComponent
- Auto-resize textarea
- Send button
- Attachment placeholder
- Keyboard shortcuts
- Loading state

### MessageItemComponent
- User/assistant message styling
- Timestamps
- Copy button
- Message status indicators
- Avatar display

### TypingIndicatorComponent
- Animated dots
- Smooth animation
- Lightweight

## 🔌 Services

### ChatService
- `sendMessage(conversationId, content)` - Send message to AI
- `subscribeToStreamingResponse(conversationId)` - Stream response
- `getConversation(conversationId)` - Get conversation history
- Manages loading states and errors

### ConversationService
- `createConversation(request)` - Create new chat
- `loadConversation(id)` - Load specific conversation
- `deleteConversation(id)` - Delete conversation
- `getCurrentConversation()` - Get active conversation
- Manages conversation state and list

### LayoutService
- `toggleSidebar()` - Toggle sidebar visibility
- `openSidebar()` - Open sidebar
- `closeSidebar()` - Close sidebar
- `isSidebarOpen()` - Get current state
- Manages responsive layout

## 🎨 Styling

### SCSS Files

1. **_variables.scss** - Theme colors and variables
2. **_layout.scss** - Layout and grid system
3. **_theme.scss** - Material theme and mixins
4. **_responsive.scss** - Responsive design breakpoints

### Responsive Breakpoints

- **xs**: < 480px (mobile)
- **sm**: 480px - 767px (tablet portrait)
- **md**: 768px - 1023px (tablet landscape)
- **lg**: 1024px - 1279px (desktop)
- **xl**: 1280px - 1919px (large desktop)
- **xxl**: 2000px+ (extra large)

### Dark Mode

Automatically detects system preference:

```
@media (prefers-color-scheme: dark) {
  // Dark mode styles
}
```

## 🔐 Security

- XSS protection via Angular sanitization
- CSRF tokens in HTTP interceptors
- Content Security Policy headers
- Input validation on all forms

## 🚀 Performance

- Lazy loading of module
- Standalone components (smaller bundle)
- OnPush change detection
- Virtual scrolling ready
- Optimized SCSS

## 🐛 Debugging

### Enable debug logging

```typescript
// In services
constructor(private logger: NgxLoggerService) {
  this.logger.debug('Chat Service initialized');
}
```

### Common Issues

**Sidebar not showing:**
- Check `LayoutService` state
- Verify breakpoint detection
- Check z-index CSS

**Messages not sending:**
- Check API endpoint
- Verify authentication
- Check browser console errors

## 📱 Mobile Optimization

- Touch-friendly buttons (48px minimum)
- Full-screen layout
- Optimized keyboard handling
- Sidebar as drawer
- Adaptive typography

## ♿ Accessibility

- ARIA labels on all buttons
- Keyboard navigation
- Color contrast ratios (WCAG AA)
- Focus management
- Screen reader support

## 📚 Documentation

- [Architecture Guide](../../docs/global/architecture-overview.md)
- [API Documentation](../../docs/microservices/infra-order/endpoints.md)
- [Styling Guide](../../docs/global/coding-standards.md)

## 🤝 Contributing

1. Follow Angular style guide
2. Use OnPush change detection
3. Write unit tests
4. Update documentation

## 📄 License

MIT

---

**Version:** 1.0.0  
**Last Updated:** 2026-05-25  
**Angular:** 15+  
**Material:** Latest

