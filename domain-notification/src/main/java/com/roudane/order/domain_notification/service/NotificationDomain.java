package com.roudane.order.domain_notification.service;

import com.roudane.order.domain_notification.port.output.json.IJsonOutPort;
import com.roudane.order.domain_notification.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.event.NotificationSentEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import com.roudane.transverse.event.enums.OrderEventType;
import com.roudane.transverse.model.OutboxModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationDomain implements IHandleNotificationEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationDomain.class);

    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final IJsonOutPort jsonOutPort;

    @Override
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: Order ID: {}, Customer ID: {}", event.getOrderId(), event.getCustomerId());
        // Simulate sending notification
        publishNotificationSentEvent(event.getOrderId(), String.valueOf(event.getCustomerId()), "ORDER_CREATED");
    }

    @Override
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        log.info("Received OrderShippedEvent: Order ID: {}, Tracking: {}", event.getOrderId(), event.getTrackingNumber());
        // Simulate sending notification
        publishNotificationSentEvent(event.getOrderId(), null, "ORDER_SHIPPED");
    }

    private void publishNotificationSentEvent(Long orderId, String customerId, String type) {
        NotificationSentEvent event = NotificationSentEvent.builder()
                .notificationId(UUID.randomUUID().toString())
                .orderId(orderId)
                .customerId(customerId)
                .type(type)
                .sentAt(LocalDateTime.now())
                .build();

        OutboxModel outboxModel = OutboxModel.builder()
                .aggregateId(String.valueOf(orderId))
                .aggregateType("NOTIFICATION")
                .eventType(OrderEventType.NOTIFICATION_SENT)
                .createdAt(LocalDateTime.now())
                .status(OutboxStatus.NEW)
                .payload(jsonOutPort.toJson(event))
                .build();

        outBoxPersistenceOutPort.saveEvent(outboxModel);
        log.info("Saved NotificationSentEvent to outbox for orderId: {}", orderId);
    }
}
