package com.example.inventory.domain.event;

import java.util.List;
import java.util.Objects;

public class OrderCancelledEvent {
    private String orderId;
    private List<OrderItemDTO> items; // Items to be released

    // Default constructor for deserialization
    public OrderCancelledEvent() {}

    public OrderCancelledEvent(String orderId, List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.items = items;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCancelledEvent that = (OrderCancelledEvent) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, items);
    }

    @Override
    public String toString() {
        return "OrderCancelledEvent{" +
                "orderId='" + orderId + '\'' +
                ", items=" + items +
                '}';
    }
}
