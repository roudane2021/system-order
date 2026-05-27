/**
 * AI Assistant Main Page
 * Integrates all components into a complete layout
 */

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Observable } from 'rxjs';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { TopbarComponent } from '../../components/topbar/topbar.component';
import { ChatWindowComponent } from '../../components/chat-window/chat-window.component';
import { PromptInputComponent } from '../../components/prompt-input/prompt-input.component';
import { LayoutService } from '../../services/layout.service';
import { ChatService } from '../../services/chat.service';
import { ConversationService } from '../../services/conversation.service';

@Component({
  selector: 'app-assistant-page',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    SidebarComponent,
    TopbarComponent,
    ChatWindowComponent,
    PromptInputComponent
  ],
  templateUrl: './assistant-page.component.html',
  styleUrls: ['./assistant-page.component.scss']
})
export class AssistantPageComponent implements OnInit {
  sidebarOpen$: Observable<boolean>;
  isMobile$: Observable<boolean>;

  constructor(
    private layoutService: LayoutService,
    private chatService: ChatService,
    private conversationService: ConversationService
  ) {
    this.sidebarOpen$ = this.layoutService.sidebarOpen$;
    this.isMobile$ = this.layoutService.isMobile$;
  }

  ngOnInit(): void {
    // Initialize with a new conversation if needed
    const currentConversation = this.conversationService.getCurrentConversation();
    if (!currentConversation) {
      this.createNewConversation();
    }
  }

  /**
   * Create new conversation
   */
  private createNewConversation(): void {
    this.conversationService.createConversation({
      title: `Chat - ${new Date().toLocaleDateString()}`
    }).subscribe();
  }

  /**
   * Handle message sent from input component
   */
  onMessageSent(message: string): void {
    const conversation = this.conversationService.getCurrentConversation();
    if (conversation) {
      this.chatService.sendMessage(conversation.id, message).subscribe(
        response => {
          console.log('Message sent:', response);
        },
        error => {
          console.error('Error sending message:', error);
        }
      );
    }
  }

  /**
   * Toggle sidebar on mobile
   */
  closeSidebarOnMobile(isMobile: boolean): void {
    if (isMobile) {
      this.layoutService.closeSidebar();
    }
  }
}

