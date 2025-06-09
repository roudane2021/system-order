package com.roudane.order.domain_order.event;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderStatus; // Assuming status is also sent
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private OrderStatus status; // Current status of the order
    private List<OrderItemModel> items;
    // Add any other relevant fields from OrderModel if needed
}
