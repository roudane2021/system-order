package com.roudane.order.domain_order.service;


import com.roudane.order.domain_order.exception.OrderInvalidException;
import com.roudane.order.domain_order.exception.OrderNotFoundException;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.input.ICreateOrderUseCase;
import com.roudane.order.domain_order.port.input.IGetOrderUseCase;
import com.roudane.order.domain_order.port.input.IListOrderUseCase;
import com.roudane.order.domain_order.port.input.IUpdateOrderUseCase;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class OrderDomain implements ICreateOrderUseCase, IGetOrderUseCase, IListOrderUseCase, IUpdateOrderUseCase {

    private final IOrderEventPublisherOutPort orderEventPublisherOutPort;
    private final IOrderPersistenceOutPort orderPersistenceOutPort;
    private final ILoggerPort loggerPort;

    /**
     * Creates a new order.
     *
     * @param orderModel the order model to be created.
     * @return the created order model with its ID.
     * @throws OrderInvalidException if the order model is null or has no items.
     */
    @Override
    public OrderModel createOrder(final OrderModel orderModel) {
        loggerPort.debug("Creating order: " + orderModel);
        if (Objects.isNull(orderModel) || CollectionUtils.isEmpty(orderModel.getItems())) {
            throw new OrderInvalidException("OrderModel is invalid");
        }
        orderModel.setStatus(OrderStatus.CREATED);
        // Enregistrement de la commande
        final OrderModel savedOrder = orderPersistenceOutPort.createOrder(orderModel);

        // Publication d'un événement
        orderEventPublisherOutPort.publisherEventOrder(orderModel);

        loggerPort.info("Order created successfully with ID: " + savedOrder.getId());
        return savedOrder;
    }

    /**
     * @param orderID
     * @return
     */
    @Override
    public OrderModel getOrder(final Long orderID) {
        loggerPort.debug("Fetching order with ID: " + orderID);

        return orderPersistenceOutPort.findOrderById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderID + " not found"));
    }

    /**
     * @return
     */
    @Override
    public Set<OrderModel> listOrder() {
        loggerPort.debug("Fetching all orders");
        return orderPersistenceOutPort.findAllOrders();
    }

    /**
     * @param orderModel
     * @return
     */
    @Override
    public OrderModel updateOrder(final OrderModel orderModel) {
        loggerPort.debug("Updating order with ID: " + orderModel);

        if (orderModel == null || orderModel.getId() == null) {
            throw new OrderInvalidException("OrderModel or ID is null");
        }

        // Vérifier si la commande existe
        OrderModel existingOrder = orderPersistenceOutPort.findOrderById(orderModel.getId())
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderModel.getId() + " not found"));

        // Mettre à jour les informations
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);


        // Publier un événement de mise à jour
        orderEventPublisherOutPort.publisherEventOrder(orderModel);

        loggerPort.info("Order updated successfully with ID: " + updatedOrder.getId());
        return updatedOrder;
    }
}
