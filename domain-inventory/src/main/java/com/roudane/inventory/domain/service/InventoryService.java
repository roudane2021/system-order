package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.event.InventoryDepletedEvent;

public interface InventoryService {
    // Option 1: Return a generic event base class or Object and then check instance type
    // Option 2: Define specific outcomes. For now, let's assume the service handles event creation.
    // It might be better for the service to return a status or specific result object,
    // and the calling layer (application service or event handler in infra) creates the Kafka event.
    // For this iteration, let's have the service create and return the specific event.

    Object handleOrderCreated(OrderCreatedEvent event); // Returns InventoryReservedEvent or InventoryDepletedEvent
    void handleOrderCancelled(OrderCancelledEvent event);
}
