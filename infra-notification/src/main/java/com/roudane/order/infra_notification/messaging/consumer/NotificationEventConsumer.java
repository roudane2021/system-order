package com.roudane.order.infra_notification.messaging.consumer;

import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {


    private final IHandleNotificationEventUseCase handleNotificationEventUseCase;

    public NotificationEventConsumer(IHandleNotificationEventUseCase handleNotificationEventUseCase) {
        this.handleNotificationEventUseCase = handleNotificationEventUseCase;
    }

    @KafkaListener(topics = "${order.events.topic.created}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "OrderCreatedEventContainerFactory")
    public void listenOrderCreatedEvent(OrderCreatedEvent event) {
            handleNotificationEventUseCase.handleOrderCreatedEvent(event);
    }

    @KafkaListener(topics = "${order.events.topic.shipped}", groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "OrderShippedEventContainerFactory")
    public void listenOrderShippedEvent(OrderShippedEvent event) {
            handleNotificationEventUseCase.handleOrderShippedEvent(event);
    }

}
