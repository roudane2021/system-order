package com.roudane.inventory.infra.web.dto;

// Using Lombok for boilerplate if available and configured, otherwise manual getters/setters
// For this script, assuming manual or that Lombok is globally configured for the project.
public class InventoryItemDto {
    private String productId;
    private int quantity;

    // Default constructor for deserialization
    public InventoryItemDto() {}

    public InventoryItemDto(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
