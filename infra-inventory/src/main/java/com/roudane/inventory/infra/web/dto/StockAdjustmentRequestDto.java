package com.roudane.inventory.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockAdjustmentRequestDto {
    private int quantity;
    private String reason;
}
