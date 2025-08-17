package com.roudane.inventory.domain.port.input;

import com.roudane.transverse.event.OrderCancelledEvent;

public interface IHhandleOrderCancelledUseCase {
    void handleOrderCancelled(OrderCancelledEvent event);
}
