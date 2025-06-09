package com.roudane.order.domain_order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventX {
    private String orderId;
    private String reason;

    private String carrier;
    private String trackingNumber;

    private String customerId;
    private LocalDateTime eventDate;
    private Set<OrderItemEvent> items;

    private OrderEventType orderEventType;
}
