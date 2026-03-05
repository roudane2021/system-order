package com.roudane.order.domain_order.model;

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
    private Object payload;
    private LocalDateTime createdAt;
    private boolean processed;
}
