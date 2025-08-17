package com.roudane.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItem {
    private String productId;
    private int quantity;
    private Long id;

    public void decrementQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        } else {
            throw new IllegalArgumentException("Not enough items in stock for product: " + productId);
        }
    }

    public void incrementQuantity(int amount) {
        this.quantity += amount;
    }

}
