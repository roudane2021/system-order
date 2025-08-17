package com.roudane.inventory.infra.messaging.consumer;

import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.inventory.domain.service.InventoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final InventoryDomain inventoryDomain;

    @Autowired
    public OrderEventConsumer(InventoryDomain inventoryDomain) {
        this.inventoryDomain= inventoryDomain;

    }

    @KafkaListener(topics = "${order.events.topic.created}", containerFactory = "orderCreatedEventContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());
        try {
            inventoryDomain.handleOrderCreated(event);
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent for orderId: " + event.getOrderId(), e);

        }
    }

    @KafkaListener(topics = "${order.events.topic.cancelled}", containerFactory = "orderCancelledEventContainerFactory")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent for orderId: {}", event.getOrderId());
        try {
            inventoryDomain.handleOrderCancelled(event);
        } catch (Exception e) {
            log.error("Error processing OrderCancelledEvent for orderId: " + event.getOrderId(), e);
        }
    }
}
