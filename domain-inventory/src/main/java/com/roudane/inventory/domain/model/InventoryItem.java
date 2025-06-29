package com.roudane.inventory.domain.model;

import lombok.Data;

import java.util.Objects;

@Data
public class InventoryItem {
    private String productId;
    private int quantity;
    // Assuming a unique identifier for an inventory record, if needed.
    // For now, productId might be sufficient if we only store total quantity per product.
    private Long id;

    public InventoryItem() {}

    public InventoryItem(String productId, int quantity, Long id) {
        this.productId = productId;
        this.quantity = quantity;
        this.id = id;
    }



    // Business logic methods can be added here e.g.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return quantity == that.quantity && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
