package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.port.out.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.event.InventoryDepletedEvent;
import com.roudane.inventory.domain.event.OrderItemDTO;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.repository.IInventoryRepositoryPort;
import com.roudane.inventory.domain.exception.InventoryDomainException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryService implements IInventoryServiceInPort {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final IInventoryRepositoryPort inventoryRepository;
    private final IInventoryEventPublisherOutPort eventPublisherPort;

    // Constructor injection
    public InventoryService(IInventoryRepositoryPort inventoryRepository, IInventoryEventPublisherOutPort eventPublisherPort) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent for orderId: {}", event.getOrderId());
        List<OrderItemDTO> requestedItems = event.getItems();
        if (requestedItems == null || requestedItems.isEmpty()) {
            log.warn("OrderCreatedEvent for orderId: {} has no items.", event.getOrderId());
            // Or throw new InventoryDomainException("Order has no items.");
            // Depending on how strict the validation should be.
            // For now, let's assume this could be a valid scenario handled by an empty InventoryReservedEvent.
            eventPublisherPort.publish(new InventoryReservedEvent(event.getOrderId(), new ArrayList<>()));
            return;
        }

        List<String> productIds = requestedItems.stream()
                                               .map(OrderItemDTO::getProductId)
                                               .collect(Collectors.toList());

        Map<String, InventoryItem> currentInventoryMap = inventoryRepository.findByProductIdIn(productIds)
                                                                            .stream()
                                                                            .collect(Collectors.toMap(InventoryItem::getProductId, item -> item));

        List<InventoryItem> itemsToUpdate = new ArrayList<>();
        List<OrderItemDTO> successfullyReservedItems = new ArrayList<>();

        for (OrderItemDTO requestedItem : requestedItems) {
            InventoryItem inventoryItem = currentInventoryMap.get(requestedItem.getProductId());

            if (inventoryItem == null || inventoryItem.getQuantity() < requestedItem.getQuantity()) {
                String depletionReason = String.format("Insufficient stock for productId: %s. Requested: %d, Available: %d",
                                                       requestedItem.getProductId(),
                                                       requestedItem.getQuantity(),
                                                       inventoryItem == null ? 0 : inventoryItem.getQuantity());
                log.warn(depletionReason + " for orderId: {}", event.getOrderId());
                // Rollback any changes made in this transaction if this were a single transaction for all items.
                // For now, we assume we try to reserve what we can or fail the whole order.
                // The current requirement implies failing the order if any item is depleted.
                eventPublisherPort.publish(new InventoryDepletedEvent(event.getOrderId(), depletionReason, requestedItems));
                return;
            }

            inventoryItem.decrementQuantity(requestedItem.getQuantity());
            itemsToUpdate.add(inventoryItem);
            successfullyReservedItems.add(requestedItem); // Add the original DTO as it represents what was reserved
        }

        // If all items can be reserved
        inventoryRepository.saveAll(itemsToUpdate);
        log.info("Inventory reserved successfully for orderId: {}. Items: {}", event.getOrderId(), successfullyReservedItems);
        eventPublisherPort.publish(new InventoryReservedEvent(event.getOrderId(), successfullyReservedItems));
    }

    @Override
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Handling OrderCancelledEvent for orderId: {}", event.getOrderId());
        List<OrderItemDTO> itemsToRelease = event.getItems();
        if (itemsToRelease == null || itemsToRelease.isEmpty()) {
            log.warn("OrderCancelledEvent for orderId: {} has no items to release.", event.getOrderId());
            return;
        }

        List<String> productIds = itemsToRelease.stream()
                                               .map(OrderItemDTO::getProductId)
                                               .collect(Collectors.toList());

        Map<String, InventoryItem> currentInventoryMap = inventoryRepository.findByProductIdIn(productIds)
                                                                            .stream()
                                                                            .collect(Collectors.toMap(InventoryItem::getProductId, item -> item));

        List<InventoryItem> itemsToUpdate = new ArrayList<>();

        for (OrderItemDTO itemToRelease : itemsToRelease) {
            InventoryItem inventoryItem = currentInventoryMap.get(itemToRelease.getProductId());
            if (inventoryItem == null) {
                // This case should ideally not happen if inventory was reserved correctly.
                // It might mean an item that was never in inventory or was removed.
                // For now, we can create a new inventory record or log a warning.
                log.warn("Attempting to release item not found in inventory: productId {}. Creating new record.", itemToRelease.getProductId());
                inventoryItem = new InventoryItem(itemToRelease.getProductId(), itemToRelease.getQuantity());
            } else {
                inventoryItem.incrementQuantity(itemToRelease.getQuantity());
            }
            itemsToUpdate.add(inventoryItem);
        }

        inventoryRepository.saveAll(itemsToUpdate);
        log.info("Inventory released successfully for orderId: {}. Items: {}", event.getOrderId(), itemsToRelease);
    }
}
