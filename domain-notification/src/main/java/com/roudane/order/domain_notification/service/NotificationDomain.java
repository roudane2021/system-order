package com.roudane.order.domain_notification.service;

import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @Service // Removed @Service annotation
public class NotificationDomain implements  IHandleNotificationEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationDomain.class);



    // Implementation for IHandleNotificationEventUseCase
    @Override
    public void handleOrderCreatedEvent(OrderEvent event) { // Changed parameter type
        // TODO: Implement logic to process the event and send notification to the customer
        log.info("Received OrderCreatedEvent: Order ID: {}, Customer ID: {}", event.getOrderId(), event.getCustomerId());
        // Example: Send email, SMS, or create an internal notification record
        // For now, just logging.
        // Potentially use createOrder() if a notification record needs to be persisted.
    }

    @Override
    public void handleOrderShippedEvent(OrderShippedEvent event) { // Changed parameter type
        // TODO: Implement logic to process the event and send notification to the customer
        log.info("Received OrderShippedEvent: Order ID: {}, Tracking: {}", event.getOrderId(), event.getTrackingNumber());
        // Example: Send email, SMS, or create an internal notification record
        // For now, just logging.
    }
}
