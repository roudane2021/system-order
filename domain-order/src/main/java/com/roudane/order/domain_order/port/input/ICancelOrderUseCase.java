package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

public interface ICancelOrderUseCase {
    OrderModel cancelOrder(final Long orderId);
}
