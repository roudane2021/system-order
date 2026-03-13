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

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderApplicationFacade  {

    private OrderApplicationService orderApplicationService;



    public OrderModel cancelOrder(Long orderId) {
        return orderApplicationService.cancelOrder(orderId);
    }


    public OrderModel confirmOrder(InventoryReservedEvent event) {
        return orderApplicationService.confirmOrder(event);
    }


    public OrderModel createOrder(OrderModel oorderModel) {
        return orderApplicationService.createOrder(oorderModel);
    }


    public OrderModel depletedOrder(InventoryDepletedEvent event) {
        return orderApplicationService.depletedOrder(event);
    }


    public OrderModel getOrder(Long orderID) {
        return orderApplicationService.getOrder(orderID);
    }


    public Set<OrderModel> listOrder() {
        return orderApplicationService.listOrder();
    }


    public PageResult<OrderModel> findOrderCriteria(List<CriteriaApplication> criteriaApplications, int page, int size) {
        return  orderApplicationService.findOrderCriteria(criteriaApplications, page, size);
    }


    public OrderModel payOrder(Long orderId) {
        return orderApplicationService.payOrder(orderId);
    }


    public OrderModel shipOrder(Long orderId, String trackingNumber) {
        return orderApplicationService.shipOrder(orderId, trackingNumber);
    }


    public OrderModel updateOrder(OrderModel NotificationModel) {
        return orderApplicationService.updateOrder(NotificationModel);
    }
}
