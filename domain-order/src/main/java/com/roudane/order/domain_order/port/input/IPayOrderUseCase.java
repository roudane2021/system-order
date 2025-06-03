package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

public interface IPayOrderUseCase {
    OrderModel payOrder(final Long orderId);
}
