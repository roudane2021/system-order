package com.roudane.inventory.infra.messaging.consumer;

import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.inventory.infra.service.InventoryApplicationFacade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final InventoryApplicationFacade inventoryApplicationFacade;



    @KafkaListener(topics = "${order.events.topic.created}", containerFactory = "genericKafkaListenerContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
            inventoryApplicationFacade.handleOrderCreated(event);

    }

    @KafkaListener(topics = "${order.events.topic.cancelled}", containerFactory = "genericKafkaListenerContainerFactory")
    public void handleOrderCancelled(OrderCancelledEvent event) {
            inventoryApplicationFacade.handleOrderCancelled(event);
    }
}
