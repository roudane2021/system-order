package com.roudane.order.infra_order.web.dto;

import com.roudane.order.domain_order.model.OrderStatus;
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
public class OrderDto {

    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long customerId;
    private OrderStatus status;
    private List<OrderItemDto> items;
}
