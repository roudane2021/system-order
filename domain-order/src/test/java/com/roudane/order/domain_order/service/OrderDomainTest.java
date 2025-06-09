package com.roudane.order.domain_order.service;

import com.roudane.order.domain_order.event.OrderCreatedEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;
import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDomainTest {

    @Mock
    private IOrderEventPublisherOutPort orderEventPublisherOutPort;
    @Mock
    private IOrderPersistenceOutPort orderPersistenceOutPort;
    @Mock
    private ILoggerPort loggerPort;

    @InjectMocks
    private OrderDomain orderDomain;

    private OrderModel sampleOrder;

    @BeforeEach
    void setUp() {
        OrderItemModel item = OrderItemModel.builder().id(1L).productId(100L).quantity(1).price(BigDecimal.TEN).build();
        sampleOrder = OrderModel.builder()
                .id(1L)
                .customerId(1L)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .items(Collections.singletonList(item))
                .orderNumber("ORD-123")
                .build();
        item.setOrderId(sampleOrder.getId());
    }

    // Test for createOrder (to ensure event publishing part is covered)
    @Test
    void createOrder_shouldPublishOrderCreatedEvent() {
        // Given
        when(orderPersistenceOutPort.createOrder(any(OrderModel.class))).thenReturn(sampleOrder);

        // When
        orderDomain.createOrder(sampleOrder);

        // Then
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(orderEventPublisherOutPort).publishOrderCreatedEvent(eventCaptor.capture());
        OrderCreatedEvent publishedEvent = eventCaptor.getValue();

        assertThat(publishedEvent.getOrderId()).isEqualTo(sampleOrder.getId());
        assertThat(publishedEvent.getCustomerId()).isEqualTo(sampleOrder.getCustomerId());
        assertThat(publishedEvent.getStatus()).isEqualTo(sampleOrder.getStatus());
    }


    @Test
    void confirmOrder_whenOrderExistsAndIsInCreatedStatus_shouldUpdateStatusToProcessing() {
        // Given
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderModel confirmedOrder = orderDomain.confirmOrder(1L);

        // Then
        assertThat(confirmedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
        verify(orderPersistenceOutPort).updateOrder(sampleOrder);
        verify(loggerPort).info("Order with ID: 1 confirmed successfully, status set to PROCESSING.");
    }

    @Test
    void confirmOrder_whenOrderNotFound_shouldThrowException() {
        // Given
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> orderDomain.confirmOrder(1L));
        assertThat(exception.getMessage()).contains("Order with ID 1 not found, cannot confirm.");
        verify(loggerPort).warn("Order not found for confirmation: 1");
    }

    @Test
    void confirmOrder_whenOrderStatusIsNotCreated_shouldReturnOrderUnchanged() {
        // Given
        sampleOrder.setStatus(OrderStatus.PAID); // Not CREATED
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(sampleOrder));

        // When
        OrderModel resultOrder = orderDomain.confirmOrder(1L);

        // Then
        assertThat(resultOrder.getStatus()).isEqualTo(OrderStatus.PAID); // Status remains unchanged
        verify(orderPersistenceOutPort, never()).updateOrder(any(OrderModel.class));
        verify(loggerPort).warn("Order 1 cannot be confirmed as its status is PAID");
    }


    @Test
    void shipOrder_whenOrderExistsAndIsInProcessibleStatus_shouldUpdateStatusToShippedAndPublishEvent() {
        // Given
        sampleOrder.setStatus(OrderStatus.PROCESSING); // Shippable status
        String trackingNumber = "TRACK123";
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderModel shippedOrder = orderDomain.shipOrder(1L, trackingNumber);

        // Then
        assertThat(shippedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPED);
        verify(orderPersistenceOutPort).updateOrder(sampleOrder);

        ArgumentCaptor<OrderShippedEvent> eventCaptor = ArgumentCaptor.forClass(OrderShippedEvent.class);
        verify(orderEventPublisherOutPort).publishOrderShippedEvent(eventCaptor.capture());
        OrderShippedEvent publishedEvent = eventCaptor.getValue();

        assertThat(publishedEvent.getOrderId()).isEqualTo(shippedOrder.getId());
        assertThat(publishedEvent.getTrackingNumber()).isEqualTo(trackingNumber);
        verify(loggerPort).info("Order with ID: 1 shipped successfully, status set to SHIPPED.");
    }

    @Test
    void shipOrder_whenOrderIsPaid_shouldUpdateStatusToShippedAndPublishEvent() {
        // Given
        sampleOrder.setStatus(OrderStatus.PAID); // Also a shippable status as per logic
        String trackingNumber = "TRACK456";
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderModel shippedOrder = orderDomain.shipOrder(1L, trackingNumber);

        // Then
        assertThat(shippedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPED);
        verify(orderPersistenceOutPort).updateOrder(sampleOrder);
        verify(orderEventPublisherOutPort).publishOrderShippedEvent(any(OrderShippedEvent.class));
    }

    @Test
    void shipOrder_whenOrderNotFound_shouldThrowException() {
        // Given
        String trackingNumber = "TRACK123";
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> orderDomain.shipOrder(1L, trackingNumber));
        assertThat(exception.getMessage()).contains("Order with ID 1 not found, cannot ship.");
        verify(loggerPort).warn("Order not found for shipping: 1");
    }

    @Test
    void shipOrder_whenOrderStatusIsNotProcessible_shouldThrowException() {
        // Given
        sampleOrder.setStatus(OrderStatus.CREATED); // Not PROCESSING or PAID
        String trackingNumber = "TRACK123";
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(sampleOrder));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> orderDomain.shipOrder(1L, trackingNumber));
        assertThat(exception.getMessage()).contains("Order 1 is in status CREATED and cannot be shipped.");
        verify(orderEventPublisherOutPort, never()).publishOrderShippedEvent(any(OrderShippedEvent.class));
    }
}
