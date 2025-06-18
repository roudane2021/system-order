package com.roudane.inventory.domain.event;

import java.util.List;
import java.util.Objects;

public class OrderCreatedEvent {
    private String orderId;
    private List<OrderItemDTO> items;
    // Add other relevant fields from order if needed, e.g., customerId

    // Default constructor for deserialization
    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, List<OrderItemDTO> items) {
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
        OrderCreatedEvent that = (OrderCreatedEvent) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, items);
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", items=" + items +
                '}';
    }
}
