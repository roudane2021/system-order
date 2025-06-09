package com.roudane.order.domain_order.port.output.event;

import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;

public interface IOrderEventPublisherOutPort {
    void publishOrderCreatedEvent(OrderEvent event);
    void publishOrderShippedEvent(OrderShippedEvent event);
    void publishOrderCancelledEvent(final OrderEvent event);
    void publishOrderUpdatedEvent(final OrderEvent event);
}
