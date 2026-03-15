package com.roudane.transverse.model;

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
    private String eventType;
    private String payload;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private OutboxStatus status;

}
