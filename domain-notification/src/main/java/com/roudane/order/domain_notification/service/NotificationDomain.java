package com.roudane.order.domain_notification.service;

import com.roudane.order.domain_notification.model.NotificationModel;
import com.roudane.order.domain_notification.port.input.ICreateNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IGetNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IListNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IUpdateNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase; // Added import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.roudane.order.domain_order.event.OrderEvent; // Moved import
import com.roudane.order.domain_order.event.OrderShippedEvent; // Moved import

import java.util.Set;

@Service
public class NotificationDomain implements ICreateNotificationUseCase, IListNotificationUseCase,
        IGetNotificationUseCase, IUpdateNotificationUseCase, IHandleNotificationEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationDomain.class);

    /**
     * @param NotificationModel
     * @return
     */
    @Override
    public NotificationModel createOrder(NotificationModel NotificationModel) {
        // TODO: Implement actual logic for creating a notification record if needed
        log.info("Creating notification: {}", NotificationModel);
        return null;
    }

    /**
     * @param notificationID
     * @return
     */
    @Override
    public NotificationModel getOrder(Long notificationID) {
        // TODO: Implement actual logic for retrieving a notification
        log.info("Getting notification with ID: {}", notificationID);
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<NotificationModel> listOrder() {
        // TODO: Implement actual logic for listing notifications
        log.info("Listing all notifications");
        return null;
    }

    /**
     * @param NotificationModel
     * @return
     */
    @Override
    public NotificationModel updateOrder(NotificationModel NotificationModel) {
        // TODO: Implement actual logic for updating a notification
        log.info("Updating notification: {}", NotificationModel);
        return null;
    }

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
