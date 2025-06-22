package com.roudane.order.domain_notification.service;

import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;
import com.roudane.order.domain_order.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDomainTest {

    // We can't directly mock the static SLF4J logger easily without PowerMock or similar.
    // For unit testing the logging, we would typically check interactions if the logger
    // was an instance field and injected. Given it's a static field,
    // we'll trust that SLF4J and the logging framework work as expected
    // and focus on whether the methods are called.
    // For more complex logic, refactoring to make logging testable would be an option.

    @InjectMocks
    private NotificationDomain notificationDomain;

    private OrderEvent orderCreatedEvent;
    private OrderShippedEvent orderShippedEvent;

    @BeforeEach
    void setUp() {
        notificationDomain = new NotificationDomain(); // Re-initialize if needed, or ensure @InjectMocks works as expected

        orderCreatedEvent = OrderEvent.builder()
                .orderId(1L)
                .customerId(100L)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(Collections.emptyList())
                .build();

        orderShippedEvent = OrderShippedEvent.builder()
                .orderId(1L)
                .shippingDate(LocalDateTime.now())
                .trackingNumber("TRK12345")
                .build();
    }

    @Test
    void handleOrderCreatedEvent_shouldProcessEvent() {
        // At this stage, the method only logs.
        // We are ensuring it runs without error and the method is covered.
        // If there were collaborators (e.g., a repository, an email service),
        // we would verify interactions with them.
        notificationDomain.handleOrderCreatedEvent(orderCreatedEvent);
        // No explicit verification needed for simple logging with a static logger in a unit test
        // unless we capture log output, which is more of an integration test concern.
    }

    @Test
    void handleOrderShippedEvent_shouldProcessEvent() {
        notificationDomain.handleOrderShippedEvent(orderShippedEvent);
        // Similar to above, primarily ensuring no errors for now.
    }

    // TODO: Add tests for CRUD methods (createOrder, getOrder, listOrder, updateOrder)
    // if their actual logic gets implemented beyond returning null/logging.
}
