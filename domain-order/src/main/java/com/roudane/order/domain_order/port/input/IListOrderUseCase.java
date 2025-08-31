package com.roudane.order.domain_order.port.input;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.transverse.criteria.CriteriaApplication;
import com.roudane.transverse.module.PageResult;

import java.util.List;
import java.util.Set;

public interface IListOrderUseCase {
    Set<OrderModel> listOrder();
    PageResult<OrderModel> findOrderCriteria(final List<CriteriaApplication> criteriaApplications, final int page, final int size);

}
