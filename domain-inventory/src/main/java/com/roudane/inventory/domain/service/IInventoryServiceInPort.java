package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.model.InventoryItem;
import java.util.List;
import java.util.Optional;
import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
// InventoryReservedEvent and InventoryDepletedEvent are not directly used in method signatures here
// but are related to the service's function through event publishing.

public interface IInventoryServiceInPort {
    // Handles events from order service
    void handleOrderCreated(OrderCreatedEvent event); // No longer returns event, publishes via output port
    void handleOrderCancelled(OrderCancelledEvent event);

    // Methods for direct inventory queries/management
    Optional<InventoryItem> findInventoryByProductId(String productId);
    List<InventoryItem> findAllInventoryItems();
    void adjustStock(String productId, int quantityChange, String reason);
}
