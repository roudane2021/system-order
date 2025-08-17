package com.roudane.order.domain_notification.service;

import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NotificationDomain implements  IHandleNotificationEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationDomain.class);

    @Override
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // TODO: Implement logic to process the event and send notification to the customer
        log.info("Received OrderCreatedEvent: Order ID: {}, Customer ID: {}", event.getOrderId(), event.getCustomerId());

    }

    @Override
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        // TODO: Implement logic to process the event and send notification to the customer
        log.info("Received OrderShippedEvent: Order ID: {}, Tracking: {}", event.getOrderId(), event.getTrackingNumber());

    }
}
