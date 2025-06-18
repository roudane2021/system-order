package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.event.InventoryDepletedEvent;

public interface IInventoryServiceInPort {
    // Option 1: Return a generic event base class or Object and then check instance type
    // Option 2: Define specific outcomes. For now, let's assume the service handles event creation.
    // It might be better for the service to return a status or specific result object,
    // and the calling layer (application service or event handler in infra) creates the Kafka event.
    // For this iteration, let's have the service create and return the specific event.
    // Now, it uses an output port to publish events, so methods are void if they were returning events.

    void handleOrderCreated(OrderCreatedEvent event); // No longer returns event, publishes via output port
    void handleOrderCancelled(OrderCancelledEvent event);
}
