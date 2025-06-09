package com.roudane.order.infra_order.adapter.output.event;

import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.event.OrderShippedEvent;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventKafkaPublisherAdapter implements IOrderEventPublisherOutPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventKafkaPublisherAdapter.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-created:order-created-events}")
    private String orderCreatedTopic;

    @Value("${kafka.topics.order-shipped:order-shipped-events}")
    private String orderShippedTopic;

    @Value("${kafka.topics.order-cancelled:order-cancelled-events}")
    private String orderCancelledTopic;

    @Value("${kafka.topics.order-updated:order-updated-events}")
    private String orderUpdatedTopic;

    @Autowired
    public OrderEventKafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreatedEvent(final OrderEvent event) {
        publishEvent(orderCreatedTopic, event.getOrderId(), event, "OrderCreatedEvent");
    }

    @Override
    public void publishOrderShippedEvent(final OrderShippedEvent event) {
        publishEvent(orderShippedTopic, event.getOrderId(), event, "OrderShippedEvent");
    }

    @Override
    public void publishOrderCancelledEvent(final OrderEvent event) {
        publishEvent(orderCancelledTopic, event.getOrderId(), event, "OrderCancelledEvent");
    }

    @Override
    public void publishOrderUpdatedEvent(final OrderEvent event) {
        publishEvent(orderUpdatedTopic, event.getOrderId(), event, "OrderUpdatedEvent");
    }

    private void publishEvent(String topic, Long orderId, Object event, String eventName) {
        LOGGER.info("Publishing {} for orderId: {}", eventName, orderId);
        kafkaTemplate.send(topic, String.valueOf(orderId), event)
                .addCallback(
                        result -> LOGGER.info("Successfully published {} for orderId: {}", eventName, orderId),
                        ex -> {
                            LOGGER.error("Failed to publish {} for orderId: {}", eventName, orderId, ex);
                            handleKafkaFailure(event, ex);
                        }
                );
    }

    public void handleKafkaFailure(Object event, Throwable ex) {
        String eventInfo = event != null ? event.toString() : "null";

        if (ex instanceof SerializationException) {
            LOGGER.error("Serialization error while publishing event: {}", eventInfo, ex);
            // Ici, tu peux par exemple ignorer ou stocker l'événement pour analyse
        } else if (ex instanceof TimeoutException) {
            LOGGER.error("Timeout error while publishing event: {}", eventInfo, ex);
            // Peut-être une ré-essai ou stockage pour traitement ultérieur

        } else if (ex instanceof KafkaException) {
            LOGGER.error("Kafka exception while publishing event: {}", eventInfo, ex);
            // Traitement générique

        } else {
            LOGGER.error("Unknown error while publishing event: {}", eventInfo, ex);

        }

    }
}
