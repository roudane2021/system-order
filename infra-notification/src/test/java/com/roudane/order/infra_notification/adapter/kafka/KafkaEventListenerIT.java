package com.roudane.order.infra_notification.adapter.kafka;

import com.roudane.order.domain_notification.port.input.IHandleNotificationEventUseCase;
import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.infra_notification.InfraNotificationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = InfraNotificationApplication.class)
@EmbeddedKafka(partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" },
        // Topics need to match those used by the listeners
        topics = {"${order.events.topic.created}", "${order.events.topic.shipped}"})
@ActiveProfiles("h2-local") // Use a test-friendly profile, h2-local seems appropriate
@DirtiesContext // Ensures Kafka broker is reset between test classes if needed
class KafkaEventListenerIT {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private IHandleNotificationEventUseCase handleNotificationEventUseCase;

    // Topic names from application.yml (ensure these are resolved correctly, might need to use fixed values or @Value)
    // For simplicity in test, using hardcoded values that should match resolved properties
    private final String orderCreatedTopic = "order-created-events";
    private final String orderShippedTopic = "order-shipped-events";

    @Test
    void shouldConsumeOrderCreatedEventAndDelegate() throws Exception {
        OrderEvent event = OrderEvent.builder()
                .orderId(1L)
                .customerId(101L)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(Collections.emptyList())
                .build();

        kafkaTemplate.send(orderCreatedTopic, event);

        // Verify that the use case method was called, with a timeout
        verify(handleNotificationEventUseCase, timeout(5000).times(1))
                .handleOrderCreatedEvent(any(OrderEvent.class));
    }

    @Test
    void shouldConsumeOrderShippedEventAndDelegate() throws Exception {
        OrderShippedEvent event = OrderShippedEvent.builder()
                .orderId(2L)
                .shippingDate(LocalDateTime.now())
                .trackingNumber("TRK54321")
                .build();

        kafkaTemplate.send(orderShippedTopic, event);

        // Verify that the use case method was called, with a timeout
        verify(handleNotificationEventUseCase, timeout(5000).times(1))
                .handleOrderShippedEvent(any(OrderShippedEvent.class));
    }
}
