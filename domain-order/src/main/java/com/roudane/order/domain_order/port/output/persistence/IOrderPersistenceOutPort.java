package com.roudane.order.domain_order.port.output.persistence;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.transverse.criteria.CriteriaApplication;
import com.roudane.transverse.module.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IOrderPersistenceOutPort {
    OrderModel updateOrder(final OrderModel orderModel);
    Set<OrderModel> findAllOrders();
    PageResult<OrderModel> findOrderCriteria(final List<CriteriaApplication> criteriaApplications, final int page, final int size);
    OrderModel getOrder(final Long orderID);
    OrderModel createOrder(final OrderModel orderModel);
    Optional<OrderModel> findOrderById(final Long orderID);

}
