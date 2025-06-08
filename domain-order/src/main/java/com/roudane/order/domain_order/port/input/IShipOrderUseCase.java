package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;

public interface IShipOrderUseCase {
    OrderModel shipOrder(Long orderId, String trackingNumber);
}
