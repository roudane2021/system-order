package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

public interface IUpdateOrderUseCase {
    OrderModel updateOrder(final OrderModel NotificationModel);

}
