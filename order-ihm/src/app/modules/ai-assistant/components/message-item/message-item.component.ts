/**
 * Message Item Component
 * Displays a single message with proper styling
 */

import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Message } from '../../models/message.model';

@Component({
  selector: 'app-message-item',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatTooltipModule],
  templateUrl: './message-item.component.html',
  styleUrls: ['./message-item.component.scss']
})
export class MessageItemComponent {
  @Input() message!: Message;
  @Input() showAvatar: boolean = true;

  /**
   * Check if message is from user
   */
  isUserMessage(): boolean {
    return this.message.role === 'user';
  }

  /**
   * Check if message is from assistant
   */
  isAssistantMessage(): boolean {
    return this.message.role === 'assistant';
  }

  /**
   * Get formatted timestamp
   */
  getFormattedTime(): string {
    return new Date(this.message.timestamp).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Copy message to clipboard
   */
  copyMessage(): void {
    navigator.clipboard.writeText(this.message.content);
  }
}

