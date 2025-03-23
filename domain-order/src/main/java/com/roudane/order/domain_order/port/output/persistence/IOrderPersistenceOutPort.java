package com.roudane.order.domain_order.port.output.persistence;

import com.roudane.order.domain_order.model.OrderModel;

import java.util.Optional;
import java.util.Set;

public interface IOrderPersistenceOutPort {
    OrderModel updateOrder(final OrderModel orderModel);
    Set<OrderModel> findAllOrders();
    OrderModel getOrder(final Long orderID);
    OrderModel createOrder(final OrderModel orderModel);
    Optional<OrderModel> findOrderById(final Long orderID);

}
