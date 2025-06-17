package com.example.inventory.domain.event;

import java.util.List;
import java.util.Objects;

public class InventoryDepletedEvent {
    private String orderId;
    private String reason; // e.g., "Insufficient stock for productId: X"
    private List<OrderItemDTO> requestedItems; // So Order service knows what was requested

    // Default constructor for deserialization
    public InventoryDepletedEvent() {}

    public InventoryDepletedEvent(String orderId, String reason, List<OrderItemDTO> requestedItems) {
        this.orderId = orderId;
        this.reason = reason;
        this.requestedItems = requestedItems;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<OrderItemDTO> getRequestedItems() {
        return requestedItems;
    }

    public void setRequestedItems(List<OrderItemDTO> requestedItems) {
        this.requestedItems = requestedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryDepletedEvent that = (InventoryDepletedEvent) o;
        return Objects.equals(orderId, that.orderId) &&
               Objects.equals(reason, that.reason) &&
               Objects.equals(requestedItems, that.requestedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, reason, requestedItems);
    }

    @Override
    public String toString() {
        return "InventoryDepletedEvent{" +
                "orderId='" + orderId + '\'' +
                ", reason='" + reason + '\'' +
                ", requestedItems=" + requestedItems +
                '}';
    }
}
