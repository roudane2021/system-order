package com.roudane.inventory.infra.messaging.producer;

import com.roudane.inventory.domain.event.InventoryDepletedEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;
import com.roudane.inventory.domain.port.out.IInventoryEventPublisherOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventPublisherAdapter implements IInventoryEventPublisherOutPort {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventPublisherAdapter.class);

    @Value("${inventory.events.topic.reserved}")
    private String inventoryReservedTopic;

    @Value("${inventory.events.topic.depleted}")
    private String inventoryDepletedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public InventoryEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(InventoryReservedEvent event) {
        log.info("Sending InventoryReservedEvent for orderId: {} to topic: {}", event.getOrderId(), inventoryReservedTopic);
        kafkaTemplate.send(inventoryReservedTopic, event.getOrderId(), event);
    }

    @Override
    public void publish(InventoryDepletedEvent event) {
        log.info("Sending InventoryDepletedEvent for orderId: {} to topic: {}", event.getOrderId(), inventoryDepletedTopic);
        kafkaTemplate.send(inventoryDepletedTopic, event.getOrderId(), event);
    }
}
