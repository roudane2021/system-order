package com.roudane.order.infra_order.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Add validation annotations later if needed
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateRequestDto {
    // Assuming only items can be updated for now.
    // Add other fields if they are updatable e.g. customerId
    private List<OrderItemDto> items;
}
