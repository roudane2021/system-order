/**
 * Prompt Input Component
 * Chat input area with auto-resize textarea
 */

import { Component, Output, EventEmitter, ViewChild, ElementRef, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-prompt-input',
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatIconModule, MatTooltipModule],
  templateUrl: './prompt-input.component.html',
  styleUrls: ['./prompt-input.component.scss']
})
export class PromptInputComponent implements OnInit, OnDestroy {
  @ViewChild('textarea') textarea!: ElementRef<HTMLTextAreaElement>;
  @Output() messageSent = new EventEmitter<string>();

  message: string = '';
  isLoading: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.chatService.isLoading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isLoading => {
        this.isLoading = isLoading;
      });
  }

  /**
   * Auto-resize textarea based on content
   */
  resizeTextarea(): void {
    const textarea = this.textarea.nativeElement;
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 200) + 'px';
  }

  /**
   * Handle keydown events
   */
  onKeyDown(event: KeyboardEvent): void {
    // Send message on Enter (Ctrl+Enter on Windows, Cmd+Enter on Mac)
    if ((event.ctrlKey || event.metaKey) && event.key === 'Enter') {
      this.sendMessage();
      event.preventDefault();
    }

    // Allow Shift+Enter for newline
    if (event.shiftKey && event.key === 'Enter') {
      return; // Default behavior
    }
  }

  /**
   * Send message
   */
  sendMessage(): void {
    if (this.message.trim() && !this.isLoading) {
      this.messageSent.emit(this.message.trim());
      this.message = '';
      this.resetTextarea();
    }
  }

  /**
   * Reset textarea height
   */
  private resetTextarea(): void {
    if (this.textarea) {
      this.textarea.nativeElement.style.height = 'auto';
    }
  }

  /**
   * Handle attachment click (placeholder)
   */
  attachFile(): void {
    console.log('Attachment clicked');
    // TODO: Implement file attachment
  }

  /**
   * Check if send button should be disabled
   */
  isSendDisabled(): boolean {
    return !this.message.trim() || this.isLoading;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

