package com.roudane.order.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private Long orderId;
    @NotNull(message = "ProductId is required")
    private Long productId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    private BigDecimal price;
}
