package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.port.input.IGetInventoryUserCase;
import com.roudane.inventory.domain.port.input.IHandleOrderCreatedUseCase;
import com.roudane.inventory.domain.port.input.IHhandleOrderCancelledUseCase;
import com.roudane.inventory.domain.port.input.IUpdateStockUserCase;
import com.roudane.inventory.domain.port.output.logger.ILoggerPort;
import com.roudane.transverse.event.*;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.exception.InventoryDomainException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.port.output.persistence.IInventoryPersistenceOutPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class InventoryDomain implements IGetInventoryUserCase, IHandleOrderCreatedUseCase, IHhandleOrderCancelledUseCase, IUpdateStockUserCase {

    private static final Logger log = LoggerFactory.getLogger(InventoryDomain.class);

    private final IInventoryPersistenceOutPort inventoryPersistenceOutPort;
    private final IInventoryEventPublisherOutPort eventPublisherPort;



    @Override
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent for orderId: {}", event.getOrderId());
        List<OrderItemEvent> requestedItems = event.getItems();
        if (requestedItems == null || requestedItems.isEmpty()) {
            log.warn("OrderCreatedEvent for orderId: {} has no items.", event.getOrderId());
            eventPublisherPort.publish(InventoryReservedEvent.builder().orderId(event.getOrderId()).reservationConfirmed(false).build());
            return;
        }

        List<String> productIds = requestedItems.stream()
                                               .map(OrderItemEvent::getProductId)
                                               .collect(Collectors.toList());

        Map<String, InventoryItem> currentInventoryMap = inventoryPersistenceOutPort.findByProductIdIn(productIds)
                                                                            .stream()
                                                                            .collect(Collectors.toMap(InventoryItem::getProductId, item -> item));

        List<InventoryItem> itemsToUpdate = new ArrayList<>();
        List<OrderItemEvent> successfullyReservedItems = new ArrayList<>();

        for (OrderItemEvent requestedItem : requestedItems) {
            InventoryItem inventoryItem = currentInventoryMap.get(requestedItem.getProductId());

            if (inventoryItem == null || inventoryItem.getQuantity() < requestedItem.getQuantity()) {
                String depletionReason = String.format("Insufficient stock for productId: %s. Requested: %d, Available: %d",
                                                       requestedItem.getProductId(),
                                                       requestedItem.getQuantity(),
                                                       inventoryItem == null ? 0 : inventoryItem.getQuantity());
                log.warn(depletionReason + " for orderId: {}", event.getOrderId());
                eventPublisherPort.publish(new InventoryDepletedEvent(event.getOrderId(), depletionReason, requestedItems));
                return;
            }

            inventoryItem.decrementQuantity(requestedItem.getQuantity());
            itemsToUpdate.add(inventoryItem);
            successfullyReservedItems.add(requestedItem);
        }

        inventoryPersistenceOutPort.saveAll(itemsToUpdate);
        log.info("Inventory reserved successfully for orderId: {}. Items: {}", event.getOrderId(), successfullyReservedItems);
        eventPublisherPort.publish(InventoryReservedEvent.builder().orderId(event.getOrderId()).reservationConfirmed(true).build());
    }

    @Override
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Handling OrderCancelledEvent for orderId: {}", event.getOrderId());
        List<OrderItemEvent> itemsToRelease = event.getItems();
        if (itemsToRelease == null || itemsToRelease.isEmpty()) {
            log.warn("OrderCancelledEvent for orderId: {} has no items to release.", event.getOrderId());
            return;
        }

        List<String> productIds = itemsToRelease.stream()
                                               .map(OrderItemEvent::getProductId)
                                               .collect(Collectors.toList());

        Map<String, InventoryItem> currentInventoryMap = inventoryPersistenceOutPort.findByProductIdIn(productIds)
                                                                            .stream()
                                                                            .collect(Collectors.toMap(InventoryItem::getProductId, item -> item));

        List<InventoryItem> itemsToUpdate = new ArrayList<>();

        for (OrderItemEvent itemToRelease : itemsToRelease) {
            InventoryItem inventoryItem = currentInventoryMap.get(itemToRelease.getProductId());
            if (inventoryItem == null) {
                log.warn("Attempting to release item not found in inventory: productId {}. Creating new record.", itemToRelease.getProductId());
                inventoryItem = InventoryItem.builder()
                        .id(0l)
                        .productId(itemToRelease.getProductId())
                        .quantity(itemToRelease.getQuantity())
                        .build();
            } else {
                inventoryItem.incrementQuantity(itemToRelease.getQuantity());
            }
            itemsToUpdate.add(inventoryItem);
        }

        inventoryPersistenceOutPort.saveAll(itemsToUpdate);
        log.info("Inventory released successfully for orderId: {}. Items: {}", event.getOrderId(), itemsToRelease);
    }

    @Override
    public Optional<InventoryItem> findInventoryByProductId(String productId) {
        log.debug("Finding inventory for productId: {}", productId);
        return inventoryPersistenceOutPort.findByProductId(productId);
    }

    @Override
    public List<InventoryItem> findAllInventoryItems() {
        log.debug("Finding all inventory items");
        return inventoryPersistenceOutPort.findAll();
    }

    @Override
    public void adjustStock(String productId, int quantityChange, String reason) {
        log.info("Adjusting stock for productId: {} by {}. Reason: {}", productId, quantityChange, reason);
        InventoryItem item = inventoryPersistenceOutPort.findByProductId(productId)
                .orElseThrow(() -> new InventoryDomainException("Cannot adjust stock. Item not found for productId: " + productId));

        if (item.getQuantity() + quantityChange < 0) {
            throw new InventoryDomainException("Stock adjustment for productId: " + productId + " would result in negative quantity.");
        }
        if (quantityChange > 0) {
            item.incrementQuantity(quantityChange);
        } else if (quantityChange < 0) {
            item.decrementQuantity(Math.abs(quantityChange));
        }

        inventoryPersistenceOutPort.save(item);
        log.info("Stock for productId: {} adjusted. New quantity: {}.", productId, item.getQuantity());
    }
}
