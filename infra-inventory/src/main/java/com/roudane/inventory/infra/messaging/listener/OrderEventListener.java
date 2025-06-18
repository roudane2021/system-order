package com.roudane.inventory.infra.messaging.listener;

import com.roudane.inventory.domain.event.InventoryDepletedEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.service.IInventoryServiceInPort;
// Removed import for InventoryEventPublisherAdapter
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final IInventoryServiceInPort inventoryService;
    // Removed InventoryEventPublisherAdapter field

    @Autowired
    public OrderEventListener(IInventoryServiceInPort inventoryService) { // Removed InventoryEventPublisherAdapter from constructor
        this.inventoryService = inventoryService;
        // Removed assignment for inventoryEventPublisherAdapter
    }

    @KafkaListener(topics = "${order.events.topic.created}", containerFactory = "orderCreatedEventContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());
        try {
            inventoryService.handleOrderCreated(event); // Domain service now handles publishing
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent for orderId: " + event.getOrderId(), e);
            // Implement error handling/retry logic if necessary
        }
    }

    @KafkaListener(topics = "${order.events.topic.cancelled}", containerFactory = "orderCancelledEventContainerFactory")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent for orderId: {}", event.getOrderId());
        try {
            inventoryService.handleOrderCancelled(event);
            // Optionally, publish an InventoryReleasedEvent if needed by other services
        } catch (Exception e) {
            log.error("Error processing OrderCancelledEvent for orderId: " + event.getOrderId(), e);
            // Implement error handling/retry logic
        }
    }
}
