package com.roudane.order.infra_notification.messaging.consumer;

import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final IHandleNotificationEventUseCase handleNotificationEventUseCase;

    public NotificationEventConsumer(IHandleNotificationEventUseCase handleNotificationEventUseCase) {
        this.handleNotificationEventUseCase = handleNotificationEventUseCase;
    }

    @KafkaListener(topics = "${order.events.topic.created}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "OrderCreatedEventContainerFactory")
    public void listenOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent from Kafka: Order ID {}", event.getOrderId());
        try {
            handleNotificationEventUseCase.handleOrderCreatedEvent(event);
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent: Order ID {}", event.getOrderId(), e);
        }
    }

    @KafkaListener(topics = "${order.events.topic.shipped}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "OrderShippedEventContainerFactory")
    public void listenOrderShippedEvent(OrderShippedEvent event) {
        log.info("Received OrderShippedEvent from Kafka: Order ID {}", event.getOrderId());
        try {
            handleNotificationEventUseCase.handleOrderShippedEvent(event);
        } catch (Exception e) {
            log.error("Error processing OrderShippedEvent: Order ID {}", event.getOrderId(), e);
        }
    }

}
