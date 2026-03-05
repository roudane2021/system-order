package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;

public interface IDepletedOrderUseCase {
    OrderModel depletedOrder(final InventoryDepletedEvent event);
}
