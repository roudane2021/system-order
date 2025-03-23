package com.roudane.order.domain_inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionModel {
    private String id;
    private String inventoryId;
    private ActionEnum action;
    private int quantity;
    private long timestamp;
}
