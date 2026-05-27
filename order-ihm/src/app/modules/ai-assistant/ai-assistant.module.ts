/**
 * AI Assistant Module
 * Main module for the AI Chat feature
 */

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatSidenavModule } from '@angular/material/sidenav';
import { LayoutModule } from '@angular/cdk/layout';

import { AiAssistantRoutingModule } from './ai-assistant-routing.module';

// Components
import { AssistantPageComponent } from './pages/assistant-page/assistant-page.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { TopbarComponent } from './components/topbar/topbar.component';
import { ChatWindowComponent } from './components/chat-window/chat-window.component';
import { PromptInputComponent } from './components/prompt-input/prompt-input.component';
import { MessageItemComponent } from './components/message-item/message-item.component';
import { TypingIndicatorComponent } from './components/typing-indicator/typing-indicator.component';

// Services
import { ChatService } from './services/chat.service';
import { ConversationService } from './services/conversation.service';
import { LayoutService } from './services/layout.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    MatSidenavModule,
    LayoutModule,
    AiAssistantRoutingModule,
    // Standalone components
    AssistantPageComponent,
    SidebarComponent,
    TopbarComponent,
    ChatWindowComponent,
    PromptInputComponent,
    MessageItemComponent,
    TypingIndicatorComponent
  ],
  providers: [
    ChatService,
    ConversationService,
    LayoutService
  ]
})
export class AiAssistantModule { }

