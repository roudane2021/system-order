package com.roudane.order.domain_inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryModel {
    private String id; // Utilise UUID ou String selon les besoins
    private String productId;
    private int totalStock;
    private int reservedStock;
}
