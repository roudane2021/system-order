/**
 * Conversation Model
 * Represents a chat conversation/thread
 */

import { Message } from './message.model';

export interface Conversation {
  id: string;
  title: string;
  description?: string;
  messages: Message[];
  createdAt: Date;
  updatedAt: Date;
  metadata?: ConversationMetadata;
  isActive: boolean;
}

export interface ConversationMetadata {
  model?: string;
  temperature?: number;
  maxTokens?: number;
  systemPrompt?: string;
  tags?: string[];
}

/**
 * Conversation Summary for the sidebar
 */
export interface ConversationSummary {
  id: string;
  title: string;
  lastMessage?: string;
  lastMessageTime?: Date;
  messageCount: number;
  isActive: boolean;
  thumbnail?: string;
}

/**
 * DTOs for API communication
 */
export interface CreateConversationRequest {
  title: string;
  systemPrompt?: string;
  metadata?: ConversationMetadata;
}

export interface CreateConversationResponse {
  id: string;
  title: string;
  createdAt: string;
}

export interface UpdateConversationRequest {
  title?: string;
  metadata?: ConversationMetadata;
}

