package com.roudane.order.domain_notification.port.input;

import com.roudane.order.domain_order.event.OrderEvent; // Import OrderEvent
import com.roudane.order.domain_order.event.OrderShippedEvent; // Import OrderShippedEvent

public interface IHandleNotificationEventUseCase {
    void handleOrderCreatedEvent(OrderEvent event); // Use OrderEvent
    void handleOrderShippedEvent(OrderShippedEvent event); // Use OrderShippedEvent
    // Add other event handling methods as needed
}
