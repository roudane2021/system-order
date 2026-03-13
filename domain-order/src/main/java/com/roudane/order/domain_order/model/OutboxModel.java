package com.roudane.order.domain_order.model;

import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.event.enums.OrderEventType;
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
    private OrderEventType eventType;
    private String payload;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private OutboxStatus status;

}
