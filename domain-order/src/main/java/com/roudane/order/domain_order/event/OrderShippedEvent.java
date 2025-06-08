package com.roudane.order.domain_order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShippedEvent {
    private Long orderId;
    private LocalDateTime shippingDate;
    private String trackingNumber;
    // Add any other relevant fields
}
