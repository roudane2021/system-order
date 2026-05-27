/**
 * AI Assistant Mock Services (For Development/Testing)
 * Replace these with real API calls in production
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { Message, Conversation, ConversationSummary } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AiAssistantMockService {
  private mockConversations: Conversation[] = [
    {
      id: '1',
      title: 'Angular Best Practices',
      description: 'Discussion about Angular patterns',
      messages: [
        {
          id: 'msg-1',
          conversationId: '1',
          role: 'user',
          content: 'What are the best practices for Angular performance?',
          status: 'sent',
          timestamp: new Date(Date.now() - 3600000)
        },
        {
          id: 'msg-2',
          conversationId: '1',
          role: 'assistant',
          content: `Here are the key Angular performance best practices:

1. **Change Detection Strategy**
   - Use OnPush for static components
   - Avoid unnecessary change detection cycles

2. **Component Lazy Loading**
   - Implement route-based lazy loading
   - Use preloading strategies wisely

3. **Bundle Optimization**
   - Enable production mode
   - Use Tree Shaking
   - Code splitting with dynamic imports

4. **Memory Management**
   - Unsubscribe from observables
   - Use trackBy in *ngFor
   - Clean up in ngOnDestroy

5. **Network Optimization**
   - HTTP compression
   - Caching strategies
   - API pagination`,
          status: 'received',
          timestamp: new Date(Date.now() - 3500000)
        }
      ],
      createdAt: new Date(Date.now() - 86400000),
      updatedAt: new Date(Date.now() - 3500000),
      isActive: true
    },
    {
      id: '2',
      title: 'TypeScript Advanced Types',
      description: 'Type system discussions',
      messages: [],
      createdAt: new Date(Date.now() - 172800000),
      updatedAt: new Date(Date.now() - 172800000),
      isActive: false
    }
  ];

  /**
   * Send message (mock)
   */
  sendMessage(conversationId: string, content: string): Observable<Message> {
    const userMessage: Message = {
      id: `msg-${Date.now()}`,
      conversationId,
      role: 'user',
      content,
      status: 'sent',
      timestamp: new Date()
    };

    const assistantMessage: Message = {
      id: `msg-${Date.now() + 1}`,
      conversationId,
      role: 'assistant',
      content: `This is a mock response to: "${content}"\n\nIn a real application, this would be connected to your AI backend service.`,
      status: 'received',
      timestamp: new Date(Date.now() + 1000)
    };

    // Simulate API delay
    return of(assistantMessage).pipe(delay(2000));
  }

  /**
   * Get all conversations
   */
  getConversations(): Observable<ConversationSummary[]> {
    const summaries = this.mockConversations.map(conv => ({
      id: conv.id,
      title: conv.title,
      lastMessage: conv.messages[conv.messages.length - 1]?.content,
      lastMessageTime: conv.updatedAt,
      messageCount: conv.messages.length,
      isActive: conv.isActive,
      thumbnail: '💬'
    }));

    return of(summaries).pipe(delay(500));
  }

  /**
   * Create new conversation
   */
  createConversation(title: string): Observable<Conversation> {
    const newConversation: Conversation = {
      id: `conv-${Date.now()}`,
      title,
      messages: [],
      createdAt: new Date(),
      updatedAt: new Date(),
      isActive: true
    };

    this.mockConversations.push(newConversation);
    return of(newConversation).pipe(delay(500));
  }

  /**
   * Get single conversation
   */
  getConversation(id: string): Observable<Conversation> {
    const conversation = this.mockConversations.find(c => c.id === id);
    return of(conversation || this.mockConversations[0]).pipe(delay(300));
  }

  /**
   * Delete conversation
   */
  deleteConversation(id: string): Observable<void> {
    const index = this.mockConversations.findIndex(c => c.id === id);
    if (index > -1) {
      this.mockConversations.splice(index, 1);
    }
    return of(void 0).pipe(delay(300));
  }
}

