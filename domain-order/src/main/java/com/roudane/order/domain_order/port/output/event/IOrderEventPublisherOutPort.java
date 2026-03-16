package com.roudane.order.domain_order.port.output.event;

import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;

import java.util.concurrent.ExecutionException;

public interface IOrderEventPublisherOutPort {
    void publishOrderCreatedEvent(OrderCreatedEvent event) throws ExecutionException, InterruptedException ;
    void publishOrderShippedEvent(OrderShippedEvent event) throws ExecutionException, InterruptedException ;
    void publishOrderCancelledEvent(final OrderCancelledEvent event) throws ExecutionException, InterruptedException ;
    void publishOrderUpdatedEvent(final OrderCreatedEvent event) throws ExecutionException, InterruptedException ;
}
