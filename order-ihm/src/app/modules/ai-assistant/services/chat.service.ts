/**
 * Chat Service
 * Handles all chat-related API calls and logic
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { tap, catchError, finalize } from 'rxjs/operators';
import { Message, SendMessageRequest, MessageResponse } from '../models/message.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = '/api/ai-assistant/chat';

  // Streams for real-time message updates
  private messageStream$ = new Subject<MessageResponse>();
  public messageReceived$ = this.messageStream$.asObservable();

  private isLoadingStream$ = new BehaviorSubject<boolean>(false);
  public isLoading$ = this.isLoadingStream$.asObservable();

  private errorStream$ = new Subject<string>();
  public error$ = this.errorStream$.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Send a message and get AI response
   */
  sendMessage(conversationId: string, content: string): Observable<MessageResponse> {
    this.isLoadingStream$.next(true);

    const request: SendMessageRequest = {
      conversationId,
      content,
      metadata: {
        timestamp: new Date().toISOString()
      }
    };

    return this.http.post<MessageResponse>(`${this.apiUrl}/send`, request).pipe(
      tap(response => {
        this.messageStream$.next(response);
      }),
      catchError(error => {
        this.errorStream$.next(error.error?.message || 'Failed to send message');
        throw error;
      }),
      finalize(() => this.isLoadingStream$.next(false))
    );
  }

  /**
   * Streaming response (WebSocket or Server-Sent Events)
   */
  subscribeToStreamingResponse(conversationId: string): Observable<string> {
    return new Observable(observer => {
      const eventSource = new EventSource(`${this.apiUrl}/stream?conversationId=${conversationId}`);

      eventSource.onmessage = (event) => {
        observer.next(event.data);
      };

      eventSource.onerror = (error) => {
        eventSource.close();
        observer.error(error);
      };

      return () => eventSource.close();
    });
  }

  /**
   * Get conversation history
   */
  getConversation(conversationId: string): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/conversations/${conversationId}/messages`);
  }

  /**
   * Clear error state
   */
  clearError(): void {
    this.errorStream$.next('');
  }

  /**
   * Get current loading state
   */
  getIsLoading(): boolean {
    return this.isLoadingStream$.value;
  }
}

