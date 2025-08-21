package com.roudane.order.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateRequestDto {
    @NotNull(message = "customerId is required")
    private Long customerId;
    @NotEmpty(message = "Items list cannot be empty")
    private List<OrderItemDto> items;
}
