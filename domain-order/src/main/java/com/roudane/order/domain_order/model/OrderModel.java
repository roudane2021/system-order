package com.roudane.order.domain_order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderModel {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long customerId;
    private OrderStatus status;
    private List<OrderItemModel> items;
}
