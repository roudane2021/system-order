package com.roudane.order.domain_order.service;


import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderItemEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.input.*;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.transverse.exception.BadRequestException;
import com.roudane.transverse.exception.InternalErrorException;
import com.roudane.transverse.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderDomain implements ICreateOrderUseCase, IGetOrderUseCase, IListOrderUseCase, IUpdateOrderUseCase, ICancelOrderUseCase, IPayOrderUseCase, IConfirmOrderUseCase, IShipOrderUseCase {

    private final IOrderEventPublisherOutPort orderEventPublisherOutPort;
    private final IOrderPersistenceOutPort orderPersistenceOutPort;
    private final ILoggerPort loggerPort;

    /**
     * Creates a new order.
     *
     * @param orderModel the order model to be created.
     * @return the created order model with its ID.
     * @throws BadRequestException if the order model is null or has no items.
     */
    @Override
    public OrderModel createOrder(final OrderModel orderModel) {
        loggerPort.debug("Creating order: " + orderModel);
        if (Objects.isNull(orderModel) || CollectionUtils.isEmpty(orderModel.getItems())) {
            throw new BadRequestException("OrderModel is invalid");
        }
        orderModel.setStatus(OrderStatus.CREATED);
        // Enregistrement de la commande
        final OrderModel savedOrder = orderPersistenceOutPort.createOrder(orderModel);



        // Publish OrderCreatedEvent
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .orderDate(savedOrder.getOrderDate())
                .items(toEventList(savedOrder.getItems()))
                .build();
        orderEventPublisherOutPort.publishOrderCreatedEvent(event);

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
                .orElseThrow(() -> new NotFoundException("Order with ID " + orderID + " not found"));
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
            throw new BadRequestException("OrderModel or ID is null");
        }

        // Vérifier si la commande existe
        OrderModel existingOrder = orderPersistenceOutPort.findOrderById(orderModel.getId())
                .orElseThrow(() -> new NotFoundException("Order with ID " + orderModel.getId() + " not found"));

        // Mettre à jour les informations
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);


        // Publish OrderUpdatedEvent
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(updatedOrder.getId())
                .customerId(updatedOrder.getCustomerId())
                .orderDate(updatedOrder.getOrderDate())
                .items(toEventList(updatedOrder.getItems()))
                .build();
        orderEventPublisherOutPort.publishOrderUpdatedEvent(event);


        loggerPort.info("Order updated successfully with ID: " + updatedOrder.getId());
        return updatedOrder;
    }

    public OrderModel cancelOrder(final Long orderId) {
        loggerPort.debug("Attempting to cancel order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for cancellation: " + orderId);
                    return new NotFoundException("Order with ID " + orderId + " not found, cannot cancel.");
                });

        orderModel.setStatus(OrderStatus.CANCELLED);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        // Publish OrderUpdatedEvent
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(updatedOrder.getId())
                .customerId(updatedOrder.getCustomerId())
                .orderDate(updatedOrder.getOrderDate())
                .items(toEventList(updatedOrder.getItems()))
                .build();
        orderEventPublisherOutPort.publishOrderUpdatedEvent(event);

        loggerPort.info("Order with ID: " + orderId + " cancelled successfully.");
        return updatedOrder;
    }

    public OrderModel payOrder(final Long orderId) {
        loggerPort.debug("Attempting to pay order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for payment: " + orderId);
                    return new NotFoundException("Order with ID " + orderId + " not found, cannot pay.");
                });

        orderModel.setStatus(OrderStatus.PAID);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        //orderEventPublisherOutPort.publisherEventOrder(updatedOrder);
        loggerPort.info("Order with ID: " + orderId + " paid successfully.");
        return updatedOrder;
    }


    @Override
    public OrderModel confirmOrder(Long orderId) {
        loggerPort.info("Attempting to confirm order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for confirmation: " + orderId);
                    return new NotFoundException("Order with ID " + orderId + " not found, cannot confirm.");
                });

        orderModel.setStatus(OrderStatus.PROCESSING);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        loggerPort.info("Order with ID: " + orderId + " confirmed successfully, status set to PROCESSING.");
        return updatedOrder;
    }


    @Override
    public OrderModel shipOrder(Long orderId, String trackingNumber) {
        loggerPort.info("Attempting to ship order with ID:  " + orderId +"with tracking: {}" + trackingNumber);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for shipping: {}" + orderId);
                    return new NotFoundException("Order with ID " + orderId + " not found, cannot ship.");
                });

        if (orderModel.getStatus() != OrderStatus.PROCESSING && orderModel.getStatus() != OrderStatus.PAID) {
            loggerPort.warn("Order "+ orderId +" cannot be shipped as its status is {}" +  orderModel.getStatus());

            throw new InternalErrorException("Order " + orderId + " is in status " + orderModel.getStatus() + " and cannot be shipped.");
        }


        orderModel.setStatus(OrderStatus.SHIPPED);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        // Publish OrderShippedEvent
        OrderShippedEvent event = OrderShippedEvent.builder()
                .orderId(updatedOrder.getId())
                .shippingDate(LocalDateTime.now())
                .trackingNumber(trackingNumber)
                .build();
        orderEventPublisherOutPort.publishOrderShippedEvent(event);

        loggerPort.info("Order with ID: " + updatedOrder.getId() + "shipped successfully, status set to SHIPPED.");
        return updatedOrder;
    }

    public static List<OrderItemEvent> toEventList(List<OrderItemModel> modelList) {
        return modelList.stream()
                .map(model -> OrderItemEvent.builder()
                        .id(model.getId())
                        .orderId(model.getOrderId())
                        .productId(model.getProductId())
                        .quantity(model.getQuantity())
                        .price(model.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
