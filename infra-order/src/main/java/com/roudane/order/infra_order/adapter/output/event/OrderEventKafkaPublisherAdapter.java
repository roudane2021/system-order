package com.roudane.order.infra_order.adapter.output.event;

import com.roudane.order.domain_order.event.OrderCreatedEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent; // Import OrderShippedEvent
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // For topic name from properties
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component // Name of component will be orderEventKafkaPublisherAdapter
public class OrderEventKafkaPublisherAdapter implements IOrderEventPublisherOutPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventKafkaPublisherAdapter.class);

    private final KafkaTemplate<String, Object> kafkaTemplate; // Use Object for generic event types or specific type

    @Value("${kafka.topics.order-created:order-created-events}") // Default topic name, configurable
    private String orderCreatedTopic;

    @Value("${kafka.topics.order-shipped:order-shipped-events}")
    private String orderShippedTopic;

    @Autowired
    public OrderEventKafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            LOGGER.info("Publishing OrderCreatedEvent for orderId: {}", event.getOrderId());
            kafkaTemplate.send(orderCreatedTopic, String.valueOf(event.getOrderId()), event);
            // Using orderId as key for partitioning (optional, but good practice)
        } catch (Exception e) {
            LOGGER.error("Error publishing OrderCreatedEvent for orderId: {}", event.getOrderId(), e);
            // Add more sophisticated error handling/retry if needed
        }
    }

    @Override
    public void publishOrderShippedEvent(OrderShippedEvent event) {
        try {
            LOGGER.info("Publishing OrderShippedEvent for orderId: {}", event.getOrderId());
            kafkaTemplate.send(orderShippedTopic, String.valueOf(event.getOrderId()), event);
        } catch (Exception e) {
            LOGGER.error("Error publishing OrderShippedEvent for orderId: {}: {}", event.getOrderId(), e.getMessage(), e);
            // Add more sophisticated error handling/retry if needed
        }
    }
}
