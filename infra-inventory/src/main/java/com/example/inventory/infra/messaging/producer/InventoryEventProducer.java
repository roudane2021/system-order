package com.example.inventory.infra.messaging.producer;

import com.example.inventory.domain.event.InventoryDepletedEvent;
import com.example.inventory.domain.event.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);

    @Value("${inventory.events.topic.reserved}")
    private String inventoryReservedTopic;

    @Value("${inventory.events.topic.depleted}")
    private String inventoryDepletedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public InventoryEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryReservedEvent(InventoryReservedEvent event) {
        log.info("Sending InventoryReservedEvent for orderId: {} to topic: {}", event.getOrderId(), inventoryReservedTopic);
        kafkaTemplate.send(inventoryReservedTopic, event.getOrderId(), event);
    }

    public void sendInventoryDepletedEvent(InventoryDepletedEvent event) {
        log.info("Sending InventoryDepletedEvent for orderId: {} to topic: {}", event.getOrderId(), inventoryDepletedTopic);
        kafkaTemplate.send(inventoryDepletedTopic, event.getOrderId(), event);
    }
}
