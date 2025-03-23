package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

public interface ICreateOrderUseCase {

    OrderModel createOrder(final OrderModel oorderModel);
}
