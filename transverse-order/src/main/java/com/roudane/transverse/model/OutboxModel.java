package com.roudane.transverse.model;

import com.roudane.transverse.enums.OutboxEventType;
import com.roudane.transverse.enums.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxModel {

    private Long id;
    private String aggregateId;
    private String aggregateType;
    private OutboxEventType eventType;
    private String payload;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private OutboxStatus status;

    private int retryCount = 0;
    private LocalDateTime lastAttemptAt;
    private String errorMessage;
    private String errorStacktrace;
    private boolean retryable = true;
}
