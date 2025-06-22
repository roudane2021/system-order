package com.roudane.order.infra_notification.adapter.kafka;

import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import com.roudane.order.domain_order.event.OrderEvent; // Moved import
import com.roudane.order.domain_order.event.OrderShippedEvent; // Moved import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListener.class);

    private final IHandleNotificationEventUseCase handleNotificationEventUseCase;

    public KafkaEventListener(IHandleNotificationEventUseCase handleNotificationEventUseCase) {
        this.handleNotificationEventUseCase = handleNotificationEventUseCase;
    }

    // TODO: Ensure the topic name matches the one in application.yml ('order.events.topic.created')
    @KafkaListener(topics = "${order.events.topic.created}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "kafkaListenerContainerFactory") // Ensure using the correct factory
    public void listenOrderCreatedEvent(OrderEvent event) { // Use OrderEvent
        log.info("Received OrderCreatedEvent from Kafka: Order ID {}", event.getOrderId());
        try {
            handleNotificationEventUseCase.handleOrderCreatedEvent(event);
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent: Order ID {}", event.getOrderId(), e);
            // TODO: Add dead-letter queue or other error handling logic
        }
    }

    // TODO: Ensure the topic name matches the one in application.yml ('order.events.topic.shipped')
    @KafkaListener(topics = "${order.events.topic.shipped}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "kafkaListenerContainerFactory") // Ensure using the correct factory
    public void listenOrderShippedEvent(OrderShippedEvent event) { // Use OrderShippedEvent
        log.info("Received OrderShippedEvent from Kafka: Order ID {}", event.getOrderId());
        try {
            handleNotificationEventUseCase.handleOrderShippedEvent(event);
        } catch (Exception e) {
            log.error("Error processing OrderShippedEvent: Order ID {}", event.getOrderId(), e);
            // TODO: Add dead-letter queue or other error handling logic
        }
    }

    // Add listeners for other events as needed
}
