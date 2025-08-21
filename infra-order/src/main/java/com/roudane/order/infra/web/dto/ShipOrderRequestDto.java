package com.roudane.order.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Add validation like @NotBlank for trackingNumber if needed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipOrderRequestDto {
    private String trackingNumber;
}
