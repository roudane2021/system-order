/**
 * Conversation Service
 * Manages conversation state and operations
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Conversation, ConversationSummary, CreateConversationRequest } from '../models/conversation.model';

@Injectable({
  providedIn: 'root'
})
export class ConversationService {
  private apiUrl = '/api/ai-assistant/conversations';

  private currentConversationStream$ = new BehaviorSubject<Conversation | null>(null);
  public currentConversation$ = this.currentConversationStream$.asObservable();

  private conversationListStream$ = new BehaviorSubject<ConversationSummary[]>([]);
  public conversationList$ = this.conversationListStream$.asObservable();

  constructor(private http: HttpClient) {
    this.loadConversations();
  }

  /**
   * Create a new conversation
   */
  createConversation(request: CreateConversationRequest): Observable<Conversation> {
    return this.http.post<Conversation>(this.apiUrl, request).pipe(
      tap(conversation => {
        this.currentConversationStream$.next(conversation);
        this.loadConversations(); // Refresh list
      })
    );
  }

  /**
   * Load all conversations for user
   */
  private loadConversations(): void {
    this.http.get<ConversationSummary[]>(this.apiUrl).subscribe(
      conversations => {
        this.conversationListStream$.next(conversations);
      }
    );
  }

  /**
   * Load specific conversation
   */
  loadConversation(conversationId: string): Observable<Conversation> {
    return this.http.get<Conversation>(`${this.apiUrl}/${conversationId}`).pipe(
      tap(conversation => {
        this.currentConversationStream$.next(conversation);
      })
    );
  }

  /**
   * Delete conversation
   */
  deleteConversation(conversationId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${conversationId}`).pipe(
      tap(() => {
        this.loadConversations();
        if (this.currentConversationStream$.value?.id === conversationId) {
          this.currentConversationStream$.next(null);
        }
      })
    );
  }

  /**
   * Get current conversation
   */
  getCurrentConversation(): Conversation | null {
    return this.currentConversationStream$.value;
  }

  /**
   * Set current conversation
   */
  setCurrentConversation(conversation: Conversation | null): void {
    this.currentConversationStream$.next(conversation);
  }
}

