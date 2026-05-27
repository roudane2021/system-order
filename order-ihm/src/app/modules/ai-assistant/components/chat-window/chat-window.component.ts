/**
 * Chat Window Component
 * Main conversation display area
 */

import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Message } from '../../models/message.model';
import { MessageItemComponent } from '../message-item/message-item.component';
import { TypingIndicatorComponent } from '../typing-indicator/typing-indicator.component';
import { ChatService } from '../../services/chat.service';
import { ConversationService } from '../../services/conversation.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-chat-window',
  standalone: true,
  imports: [CommonModule, MessageItemComponent, TypingIndicatorComponent],
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.scss']
})
export class ChatWindowComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef<HTMLDivElement>;

  messages: Message[] = [];
  isLoading: boolean = false;
  private shouldScroll = false;
  private destroy$ = new Subject<void>();

  constructor(
    private chatService: ChatService,
    private conversationService: ConversationService
  ) {}

  ngOnInit(): void {
    // Load conversation messages
    this.conversationService.currentConversation$
      .pipe(takeUntil(this.destroy$))
      .subscribe(conversation => {
        if (conversation) {
          this.messages = conversation.messages;
          this.shouldScroll = true;
        } else {
          this.messages = [];
        }
      });

    // Listen for new messages
    this.chatService.messageReceived$
      .pipe(takeUntil(this.destroy$))
      .subscribe(message => {
        this.messages.push({
          id: message.id,
          conversationId: '',
          role: message.role,
          content: message.content,
          status: 'received',
          timestamp: new Date(message.timestamp),
          metadata: message.metadata
        });
        this.shouldScroll = true;
      });

    // Listen to loading state
    this.chatService.isLoading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isLoading => {
        this.isLoading = isLoading;
      });
  }

  /**
   * Auto-scroll to bottom on new messages
   */
  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  /**
   * Scroll to bottom of messages
   */
  private scrollToBottom(): void {
    try {
      const container = this.messagesContainer.nativeElement;
      container.scrollTop = container.scrollHeight;
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }

  /**
   * Check if should show avatar (only for message group transitions)
   */
  shouldShowAvatar(index: number): boolean {
    if (index === 0) return true;
    return this.messages[index].role !== this.messages[index - 1].role;
  }

  /**
   * Get placeholder message for empty conversation
   */
  getEmptyMessage(): string {
    return 'Start a new conversation! Ask me anything...';
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

