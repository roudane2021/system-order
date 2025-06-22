package com.roudane.order.infra_order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Add validation annotations later if needed (e.g., @NotNull, @NotEmpty)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateRequestDto {
    private Long customerId;
    private List<OrderItemDto> items; // Reusing OrderItemDto
}
