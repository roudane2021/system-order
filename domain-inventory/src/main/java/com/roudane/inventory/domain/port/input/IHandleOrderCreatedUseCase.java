package com.roudane.inventory.domain.port.input;

import com.roudane.transverse.event.OrderCreatedEvent;

public interface IHandleOrderCreatedUseCase {
    void handleOrderCreated(OrderCreatedEvent event);
}
