package com.roudane.order.domain_order.port.output.event;

import com.roudane.order.domain_order.event.OrderCreatedEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent; // Import the new event

public interface IOrderEventPublisherOutPort {
    void publishOrderCreatedEvent(OrderCreatedEvent event);
    void publishOrderShippedEvent(OrderShippedEvent event);
    // Add other event publishing methods here later, e.g., publishOrderShippedEvent
}
