/**
 * Sidebar Component
 * Displays chat history and user controls
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ConversationService } from '../../services/conversation.service';
import { ConversationSummary } from '../../models/conversation.model';

@Component({
  selector: 'app-ai-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatMenuModule,
    MatDividerModule,
    RouterModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit, OnDestroy {
  conversations: ConversationSummary[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private conversationService: ConversationService
  ) {}

  ngOnInit(): void {
    this.conversationService.conversationList$
      .pipe(takeUntil(this.destroy$))
      .subscribe(conversations => {
        this.conversations = conversations;
      });
  }

  /**
   * Create new conversation
   */
  createNewChat(): void {
    this.conversationService.createConversation({
      title: `New Chat - ${new Date().toLocaleDateString()}`
    }).subscribe();
  }

  /**
   * Select conversation
   */
  selectConversation(conversation: ConversationSummary): void {
    this.conversationService.loadConversation(conversation.id).subscribe();
  }

  /**
   * Delete conversation
   */
  deleteConversation(event: Event, conversationId: string): void {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this conversation?')) {
      this.conversationService.deleteConversation(conversationId).subscribe();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

