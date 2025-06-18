package com.roudane.inventory.infra.web.dto;

public class StockAdjustmentRequestDto {
    private int quantity; // Can be positive (add) or negative (remove), or a new total quantity
    private String reason; // Optional: reason for adjustment

    public StockAdjustmentRequestDto() {}

    public StockAdjustmentRequestDto(int quantity, String reason) {
        this.quantity = quantity;
        this.reason = reason;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
