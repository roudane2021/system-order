/**
 * Message Model
 * Represents a single message in a conversation
 */

export type MessageRole = 'user' | 'assistant' | 'system';
export type MessageStatus = 'sending' | 'sent' | 'error' | 'received';

export interface Message {
  id: string;
  conversationId: string;
  role: MessageRole;
  content: string;
  status: MessageStatus;
  timestamp: Date;
  metadata?: MessageMetadata;
}

export interface MessageMetadata {
  tokens?: number;
  model?: string;
  temperature?: number;
  citations?: Citation[];
}

export interface Citation {
  title: string;
  url: string;
  source: string;
}

/**
 * DTO for sending messages to backend
 */
export interface SendMessageRequest {
  conversationId: string;
  content: string;
  metadata?: Record<string, any>;
}

/**
 * DTO for receiving messages from backend
 */
export interface MessageResponse {
  id: string;
  role: MessageRole;
  content: string;
  timestamp: string;
  metadata?: MessageMetadata;
}

