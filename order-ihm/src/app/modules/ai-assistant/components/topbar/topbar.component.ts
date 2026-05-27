/**
 * Top Bar Component
 * Header with title and controls
 */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ConversationService } from '../../services/conversation.service';
import { LayoutService } from '../../services/layout.service';
import { Conversation } from '../../models/conversation.model';

@Component({
  selector: 'app-ai-topbar',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatMenuModule
  ],
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit, OnDestroy {
  currentConversation: Conversation | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private conversationService: ConversationService,
    private layoutService: LayoutService
  ) {}

  ngOnInit(): void {
    this.conversationService.currentConversation$
      .pipe(takeUntil(this.destroy$))
      .subscribe(conversation => {
        this.currentConversation = conversation;
      });
  }

  /**
   * Toggle sidebar
   */
  toggleSidebar(): void {
    this.layoutService.toggleSidebar();
  }

  /**
   * Share conversation
   */
  shareConversation(): void {
    if (this.currentConversation) {
      console.log('Share conversation:', this.currentConversation.id);
      // TODO: Implement sharing
    }
  }

  /**
   * Export conversation
   */
  exportConversation(): void {
    if (this.currentConversation) {
      const json = JSON.stringify(this.currentConversation, null, 2);
      const blob = new Blob([json], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `${this.currentConversation.title}.json`;
      link.click();
      URL.revokeObjectURL(url);
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

