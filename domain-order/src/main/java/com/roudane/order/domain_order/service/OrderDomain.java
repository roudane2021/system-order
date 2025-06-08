package com.roudane.order.domain_order.service;


import com.roudane.order.domain_order.exception.OrderInvalidException;
import com.roudane.order.domain_order.exception.OrderNotFoundException;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.input.ICreateOrderUseCase;
import com.roudane.order.domain_order.port.input.IGetOrderUseCase;
import com.roudane.order.domain_order.port.input.IListOrderUseCase;
import com.roudane.order.domain_order.port.input.IUpdateOrderUseCase;
import com.roudane.order.domain_order.port.input.ICancelOrderUseCase;
import com.roudane.order.domain_order.port.input.IPayOrderUseCase;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.domain_order.event.OrderCreatedEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent; // Added import
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime; // Added import
import java.util.Objects;
import java.util.Set;

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
        // orderEventPublisherOutPort.publisherEventOrder(orderModel);

        // Publish OrderCreatedEvent
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .orderDate(savedOrder.getOrderDate())
                .status(savedOrder.getStatus())
                .items(savedOrder.getItems()) // Assuming items are part of the event
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

    public OrderModel cancelOrder(final Long orderId) {
        loggerPort.debug("Attempting to cancel order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for cancellation: " + orderId);
                    return new OrderNotFoundException("Order with ID " + orderId + " not found, cannot cancel.");
                });

        // Add logic to check if order can be cancelled (e.g., not already shipped) if necessary
        // For now, directly setting to CANCELLED
        orderModel.setStatus(OrderStatus.CANCELLED);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel); // Assuming updateOrder handles status changes

        orderEventPublisherOutPort.publisherEventOrder(updatedOrder); // Pass the updated order
        loggerPort.info("Order with ID: " + orderId + " cancelled successfully.");
        return updatedOrder;
    }

    public OrderModel payOrder(final Long orderId) {
        loggerPort.debug("Attempting to pay order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for payment: " + orderId);
                    return new OrderNotFoundException("Order with ID " + orderId + " not found, cannot pay.");
                });

        // Add logic to check if order can be paid (e.g., is in CREATED status) if necessary
        // For now, directly setting to PAID
        orderModel.setStatus(OrderStatus.PAID);
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel); // Assuming updateOrder handles status changes

        orderEventPublisherOutPort.publisherEventOrder(updatedOrder); // Pass the updated order
        loggerPort.info("Order with ID: " + orderId + " paid successfully.");
        return updatedOrder;
    }

    // New method implementation:
    @Override
    public OrderModel confirmOrder(Long orderId) {
        loggerPort.info("Attempting to confirm order with ID: " + orderId);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for confirmation: " + orderId);
                    // Consider using a more specific domain exception
                    return new RuntimeException("Order with ID " + orderId + " not found, cannot confirm.");
                });

        // Add logic to check if order can be confirmed (e.g., is in CREATED status)
        if (orderModel.getStatus() != OrderStatus.CREATED) {
            loggerPort.warn("Order {} cannot be confirmed as its status is {}", orderId, orderModel.getStatus());
            // Or throw an exception, or handle as per business rules
            return orderModel; // Or throw InvalidOrderStatusChangeException
        }

        orderModel.setStatus(OrderStatus.PROCESSING); // Or a new status like CONFIRMED
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        // Optionally, publish another event like OrderConfirmedEvent if needed by other services
        // For now, just updating status.

        loggerPort.info("Order with ID: " + orderId + " confirmed successfully, status set to PROCESSING.");
        return updatedOrder;
    }

    // New method implementation:
    @Override
    public OrderModel shipOrder(Long orderId, String trackingNumber) {
        loggerPort.info("Attempting to ship order with ID: {} with tracking: {}", orderId, trackingNumber);
        OrderModel orderModel = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> {
                    loggerPort.warn("Order not found for shipping: {}", orderId);
                    return new RuntimeException("Order with ID " + orderId + " not found, cannot ship."); // Domain specific exception
                });

        // Add logic to check if order can be shipped (e.g., is in PROCESSING/CONFIRMED status)
        if (orderModel.getStatus() != OrderStatus.PROCESSING && orderModel.getStatus() != OrderStatus.PAID) { // Assuming PAID orders can also be shipped
            loggerPort.warn("Order {} cannot be shipped as its status is {}", orderId, orderModel.getStatus());
            // Or throw InvalidOrderStatusChangeException
            // For now, let's throw an exception if not in a shippable state.
            throw new RuntimeException("Order " + orderId + " is in status " + orderModel.getStatus() + " and cannot be shipped.");
        }

        // In a real system, you might have a specific field for trackingNumber in OrderModel.
        // For now, we'll assume it's managed externally or just used for the event.
        // If OrderModel needs a trackingNumber field, it should be added there.
        // OrderModel currentOrderModel = orderModel; // If no direct fields to update on OrderModel besides status

        orderModel.setStatus(OrderStatus.SHIPPED); // Assuming SHIPPED is a valid status
        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(orderModel);

        // Publish OrderShippedEvent
        OrderShippedEvent event = OrderShippedEvent.builder()
                .orderId(updatedOrder.getId())
                .shippingDate(LocalDateTime.now()) // Assuming shipping date is now
                .trackingNumber(trackingNumber)
                .build();
        orderEventPublisherOutPort.publishOrderShippedEvent(event); // New method on publisher port

        loggerPort.info("Order with ID: {} shipped successfully, status set to SHIPPED.", updatedOrder.getId());
        return updatedOrder;
    }
}
