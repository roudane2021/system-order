package com.roudane.order.domain_order.port.output.event;

import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;

public interface IOrderEventPublisherOutPort {
    void publishOrderCreatedEvent(OrderCreatedEvent event);
    void publishOrderShippedEvent(OrderShippedEvent event);
    void publishOrderCancelledEvent(final OrderCreatedEvent event);
    void publishOrderUpdatedEvent(final OrderCreatedEvent event);
}
