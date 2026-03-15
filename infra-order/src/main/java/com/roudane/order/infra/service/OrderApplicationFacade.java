package com.roudane.order.infra.service;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.port.input.*;
import com.roudane.order.domain_order.service.OrderApplicationService;
import com.roudane.transverse.criteria.CriteriaApplication;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import com.roudane.transverse.module.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderApplicationFacade  {

    private final OrderApplicationService orderApplicationService;



    public OrderModel cancelOrder(Long orderId) {
        return orderApplicationService.cancelOrder(orderId);
    }


    public OrderModel confirmOrder(InventoryReservedEvent event) {
        return orderApplicationService.confirmOrder(event);
    }


    @Transactional
    public OrderModel createOrder(OrderModel oorderModel) {
        return orderApplicationService.createOrder(oorderModel);
    }


    @Transactional
    public OrderModel depletedOrder(InventoryDepletedEvent event) {
        return orderApplicationService.depletedOrder(event);
    }


    @Transactional
    public OrderModel getOrder(Long orderID) {
        return orderApplicationService.getOrder(orderID);
    }


    @Transactional
    public Set<OrderModel> listOrder() {
        return orderApplicationService.listOrder();
    }


    @Transactional
    public PageResult<OrderModel> findOrderCriteria(List<CriteriaApplication> criteriaApplications, int page, int size) {
        return  orderApplicationService.findOrderCriteria(criteriaApplications, page, size);
    }


    @Transactional
    public OrderModel payOrder(Long orderId) {
        return orderApplicationService.payOrder(orderId);
    }


    @Transactional
    public OrderModel shipOrder(Long orderId, String trackingNumber) {
        return orderApplicationService.shipOrder(orderId, trackingNumber);
    }


    @Transactional
    public OrderModel updateOrder(OrderModel NotificationModel) {
        return orderApplicationService.updateOrder(NotificationModel);
    }
}
