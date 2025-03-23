package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

import java.util.Set;

public interface IListOrderUseCase {
    Set<OrderModel> listOrder();

}
