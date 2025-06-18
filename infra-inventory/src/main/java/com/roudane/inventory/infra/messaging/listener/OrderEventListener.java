package com.roudane.inventory.infra.messaging.listener;

import com.roudane.inventory.domain.event.InventoryDepletedEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.event.OrderCancelledEvent;
import com.roudane.inventory.domain.event.OrderCreatedEvent;
import com.roudane.inventory.domain.service.InventoryService;
import com.roudane.inventory.infra.messaging.producer.InventoryEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    @Autowired
    public OrderEventListener(InventoryService inventoryService, InventoryEventProducer inventoryEventProducer) {
        this.inventoryService = inventoryService;
        this.inventoryEventProducer = inventoryEventProducer;
    }

    @KafkaListener(topics = "${order.events.topic.created}", containerFactory = "orderCreatedEventContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());
        try {
            Object resultEvent = inventoryService.handleOrderCreated(event);
            if (resultEvent instanceof InventoryReservedEvent) {
                inventoryEventProducer.sendInventoryReservedEvent((InventoryReservedEvent) resultEvent);
            } else if (resultEvent instanceof InventoryDepletedEvent) {
                inventoryEventProducer.sendInventoryDepletedEvent((InventoryDepletedEvent) resultEvent);
            }
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
