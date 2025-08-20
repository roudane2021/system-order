package com.roudane.inventory.infra.messaging.producer;

import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryEventPublisherAdapter implements IInventoryEventPublisherOutPort {



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
        publishEvent(inventoryReservedTopic, event.getOrderId(), event);
    }

    @Override
    public void publish(InventoryDepletedEvent event) {
        log.info("Sending InventoryDepletedEvent for orderId: {} to topic: {}", event.getOrderId(), inventoryDepletedTopic);
        publishEvent(inventoryDepletedTopic, event.getOrderId(), event);
    }

    private void publishEvent(String topic, Long orderId, Object event) {
        kafkaTemplate.send(topic, String.valueOf(orderId), event)
                .addCallback(
                        result -> log.info("Successfully published {} for orderId: {}", topic, orderId),
                        ex -> {
                            log.error("Failed to publish {} for orderId: {}", topic, orderId, ex);
                            handleKafkaFailure(event, ex);
                        }
                );
    }

    public void handleKafkaFailure(Object event, Throwable ex) {
        String eventInfo = event != null ? event.toString() : "null";

        if (ex instanceof SerializationException) {
            log.error("Serialization error while publishing event: {}", eventInfo, ex);
        } else if (ex instanceof TimeoutException) {
            log.error("Timeout error while publishing event: {}", eventInfo, ex);

        } else if (ex instanceof KafkaException) {
            log.error("Kafka exception while publishing event: {}", eventInfo, ex);
        } else {
            log.error("Unknown error while publishing event: {}", eventInfo, ex);

        }

    }
}
