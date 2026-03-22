package com.roudane.order.domain_order.service;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.output.json.IJsonOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.domain_order.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import com.roudane.transverse.exception.BadRequestException;
import com.roudane.transverse.exception.InternalErrorException;
import com.roudane.transverse.exception.NotFoundException;
import com.roudane.transverse.model.OutboxModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceTest {

    @InjectMocks
    private OrderApplicationService orderApplicationService;

    @Mock
    private IOrderPersistenceOutPort orderPersistenceOutPort;

    @Mock
    private IOutBoxPersistenceOutPort outBoxPersistenceOutPort;

    @Mock
    private ILoggerPort loggerPort;

    @Mock
    private IJsonOutPort jsonOutPort;

    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        
        orderModel = OrderModel.builder()
                .id(1L)
                .orderNumber("ORD-123")
                .orderDate(LocalDateTime.now())
                .customerId(1L)
                .status(OrderStatus.CREATED)
                .items(List.of(
                        OrderItemModel.builder()
                                .id(1L)
                                .orderId(1L)
                                .productId("1L")
                                .quantity(1)
                                .price(new BigDecimal("10.00"))
                                .build()
                ))
                .build();
    }

    @Test
    void createOrder_shouldCreateOrderAndPublishEvent() {
        when(orderPersistenceOutPort.createOrder(any(OrderModel.class))).thenReturn(orderModel);
        when(jsonOutPort.toJson(any())).thenReturn("{}");

        OrderModel createdOrder = orderApplicationService.createOrder(orderModel);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        verify(orderPersistenceOutPort, times(1)).createOrder(any(OrderModel.class));
        verify(outBoxPersistenceOutPort, times(1)).saveEvent(any(OutboxModel.class));
    }

    @Test
    void createOrder_shouldThrowBadRequestException_whenOrderIsInvalid() {
        assertThrows(BadRequestException.class, () -> orderApplicationService.createOrder(null));
        assertThrows(BadRequestException.class, () -> orderApplicationService.createOrder(new OrderModel()));
    }

    @Test
    void getOrder_shouldReturnOrder_whenOrderExists() {
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));

        OrderModel foundOrder = orderApplicationService.getOrder(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
    }

    @Test
    void getOrder_shouldThrowNotFoundException_whenOrderDoesNotExist() {
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderApplicationService.getOrder(1L));
    }

    @Test
    void cancelOrder_shouldCancelOrder_whenOrderExists() {
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenReturn(orderModel);

        orderApplicationService.cancelOrder(1L);

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        verify(orderPersistenceOutPort).updateOrder(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELLED, orderCaptor.getValue().getStatus());
    }

    @Test
    void cancelOrder_shouldThrowNotFoundException_whenOrderDoesNotExist() {
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderApplicationService.cancelOrder(1L));
    }

    @Test
    void payOrder_shouldUpdateStatusToPaid() {
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenReturn(orderModel);

        orderApplicationService.payOrder(1L);

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        verify(orderPersistenceOutPort).updateOrder(orderCaptor.capture());
        assertEquals(OrderStatus.PAID, orderCaptor.getValue().getStatus());
    }

    @Test
    void confirmOrder_shouldUpdateStatusToProcessing_whenReservationIsConfirmed() {
        InventoryReservedEvent event = InventoryReservedEvent.builder().orderId(1L).reservationConfirmed(true).build();
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenReturn(orderModel);

        orderApplicationService.confirmOrder(event);

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        verify(orderPersistenceOutPort).updateOrder(orderCaptor.capture());
        assertEquals(OrderStatus.PROCESSING, orderCaptor.getValue().getStatus());
    }

    @Test
    void confirmOrder_shouldThrowNotFoundException_whenReservationIsNotConfirmed() {
        InventoryReservedEvent event = InventoryReservedEvent.builder().orderId(1L).reservationConfirmed(false).build();
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));

        assertThrows(NotFoundException.class, () -> orderApplicationService.confirmOrder(event));
    }

    @Test
    void shipOrder_shouldUpdateStatusToShipped_whenOrderIsInProcessing() {
        orderModel.setStatus(OrderStatus.PROCESSING);
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenReturn(orderModel);

        orderApplicationService.shipOrder(1L, "TRACK-123");

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        verify(orderPersistenceOutPort).updateOrder(orderCaptor.capture());
        assertEquals(OrderStatus.SHIPPED, orderCaptor.getValue().getStatus());
    }

    @Test
    void shipOrder_shouldThrowInternalErrorException_whenOrderStatusIsNotValidForShipping() {
        orderModel.setStatus(OrderStatus.CREATED);
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));

        assertThrows(InternalErrorException.class, () -> orderApplicationService.shipOrder(1L, "TRACK-123"));
    }

    @Test
    void depletedOrder_shouldUpdateStatusToDeclined() {
        InventoryDepletedEvent event = InventoryDepletedEvent.builder().orderId(1L).build();
        when(orderPersistenceOutPort.findOrderById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistenceOutPort.updateOrder(any(OrderModel.class))).thenReturn(orderModel);

        orderApplicationService.depletedOrder(event);

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        verify(orderPersistenceOutPort).updateOrder(orderCaptor.capture());
        assertEquals(OrderStatus.DECLINED, orderCaptor.getValue().getStatus());
    }
}
