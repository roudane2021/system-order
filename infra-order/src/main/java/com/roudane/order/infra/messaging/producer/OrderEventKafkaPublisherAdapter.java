package com.roudane.order.infra.messaging.producer;

import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventKafkaPublisherAdapter implements IOrderEventPublisherOutPort {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-created:order-created-events}")
    private String orderCreatedTopic;

    @Value("${kafka.topics.order-shipped:order-shipped-events}")
    private String orderShippedTopic;

    @Value("${kafka.topics.order-cancelled:order-cancelled-events}")
    private String orderCancelledTopic;

    @Value("${kafka.topics.order-updated:order-updated-events}")
    private String orderUpdatedTopic;

    public OrderEventKafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreatedEvent(final OrderCreatedEvent event) {
        publishEvent(orderCreatedTopic, event.getOrderId(), event);
    }

    @Override
    public void publishOrderShippedEvent(final OrderShippedEvent event) {
        publishEvent(orderShippedTopic, event.getOrderId(), event);
    }

    @Override
    public void publishOrderCancelledEvent(final OrderCreatedEvent event) {
        publishEvent(orderCancelledTopic, event.getOrderId(), event);
    }

    @Override
    public void publishOrderUpdatedEvent(final OrderCreatedEvent event) {
        publishEvent(orderUpdatedTopic, event.getOrderId(), event);
    }

    private void publishEvent(String topic, Long orderId, Object event) {
        kafkaTemplate.send(topic, String.valueOf(orderId), event)
                .addCallback(
                        result -> log.info("Successfully published {} for orderId: {}", topic, orderId),
                        ex -> {
                            handleKafkaFailure(event, ex);
                        }
                );
    }

    public void handleKafkaFailure(Object event, Throwable ex) {
        String eventInfo = event != null ? event.toString() : "null";

        if (ex instanceof SerializationException) {
            log.error("Serialization error while publishing event: {}", eventInfo, ex);
        } else if (ex instanceof TimeoutException) {
            log.error("Timeout error while publishing event: {}", eventInfo, ex);

        } else if (ex instanceof KafkaException) {
            log.error("Kafka exception while publishing event: {}", eventInfo, ex);
        } else {
            log.error("Unknown error while publishing event: {}", eventInfo, ex);

        }

    }
}
