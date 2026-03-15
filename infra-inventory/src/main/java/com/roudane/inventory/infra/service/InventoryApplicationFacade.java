package com.roudane.inventory.infra.service;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.service.InventoryDomain;
import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryApplicationFacade {

    private final InventoryDomain inventoryDomain;

    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        inventoryDomain.handleOrderCreated(event);
    }

    @Transactional
    public void handleOrderCancelled(OrderCancelledEvent event) {
        inventoryDomain.handleOrderCancelled(event);
    }

    public Optional<InventoryItem> findInventoryByProductId(String productId) {
        return inventoryDomain.findInventoryByProductId(productId);
    }

    public List<InventoryItem> findAllInventoryItems() {
        return inventoryDomain.findAllInventoryItems();
    }

    @Transactional
    public void adjustStock(String productId, int quantityChange, String reason) {
        inventoryDomain.adjustStock(productId, quantityChange, reason);
    }
}
