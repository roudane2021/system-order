package com.roudane.order.infra_order.web.dto;

import com.roudane.order.domain_order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateResponseDto {
    private Long id;
    private OrderStatus status;
}
