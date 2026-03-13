package com.roudane.order.infra.messaging.producer;

import com.roudane.order.domain_notification.port.output.event.INotificationEventPublisherOutPort;
import com.roudane.transverse.event.NotificationSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisherAdapter implements INotificationEventPublisherOutPort {

    @Value("${notification.events.topic.sent:notification-sent}")
    private String notificationSentTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(NotificationSentEvent event) {
        log.info("Sending NotificationSentEvent for orderId: {} to topic: {}", event.getOrderId(), notificationSentTopic);
        kafkaTemplate.send(notificationSentTopic, String.valueOf(event.getOrderId()), event)
                .addCallback(
                        result -> log.info("Successfully published notification sent event for orderId: {}", event.getOrderId()),
                        ex -> log.error("Failed to publish notification sent event for orderId: {}", event.getOrderId(), ex)
                );
    }
}
