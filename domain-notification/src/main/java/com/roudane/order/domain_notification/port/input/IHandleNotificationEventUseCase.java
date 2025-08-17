package com.roudane.order.domain_notification.port.input;


import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;

public interface IHandleNotificationEventUseCase {
    void handleOrderCreatedEvent(OrderCreatedEvent event);
    void handleOrderShippedEvent(OrderShippedEvent event);
}
