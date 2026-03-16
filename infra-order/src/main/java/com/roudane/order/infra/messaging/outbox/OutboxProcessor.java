package com.roudane.order.infra.messaging.outbox;

import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.json.IJsonOutPort;
import com.roudane.order.domain_order.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
import com.roudane.transverse.model.OutboxModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.type.SerializationException;
import org.springframework.kafka.KafkaException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final IOrderEventPublisherOutPort orderEventPublisherOutPort;
    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final IJsonOutPort jsonOutPort;
    private final OutboxProperties outboxProperties;


    @Scheduled(fixedDelayString = "${outbox.polling-delay}")
    public void processOutboxMessages() {
        List<OutboxModel> events = outBoxPersistenceOutPort.lockNextEvents(outboxProperties.getLimit(), outboxProperties.getMaxRetries(), outboxProperties.getDelaySeconds());

        if (CollectionUtils.isEmpty(events)) {
            events = List.of();
        }

        log.debug("Processing {} pending outbox messages", events.size());

        events.forEach(this::processEvent);
    }

    private void processEvent(OutboxModel event) {
        try {
            publishEvent(event);
            outBoxPersistenceOutPort.markAsSent(event.getId());
            log.info("Successfully processed outbox message with ID: {}", event.getId());
        } catch (Exception ex) {
            handleProcessingError(event, ex);
        }
    }

    private void publishEvent(OutboxModel event) throws ExecutionException, InterruptedException {
        switch (event.getEventType()) {
            case ORDER_CREATED -> {
                OrderCreatedEvent created = (OrderCreatedEvent) jsonOutPort.readValue((String) event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisherOutPort.publishOrderCreatedEvent(created);
            }
            case ORDER_SHIPPED -> {
                OrderShippedEvent shipped = (OrderShippedEvent) jsonOutPort.readValue((String) event.getPayload(), OrderShippedEvent.class);
                orderEventPublisherOutPort.publishOrderShippedEvent(shipped);
            }
            case ORDER_CANCELLED -> {
                OrderCancelledEvent cancelled = (OrderCancelledEvent) jsonOutPort.readValue((String) event.getPayload(), OrderCancelledEvent.class);
                orderEventPublisherOutPort.publishOrderCancelledEvent(cancelled);
            }
        }
    }

    private void handleProcessingError(OutboxModel event, Exception ex) {
        Throwable cause = ex;

        while (cause.getCause() != null && cause instanceof ExecutionException) {
            cause = cause.getCause();
        }

        boolean retryable = determineIfRetryable(cause);

        log.error("Outbox event {} failed: {}", event.getId(), cause.getMessage(), cause);

        outBoxPersistenceOutPort.markAsError(event.getId(), cause, retryable);
    }

    private boolean determineIfRetryable(Throwable cause) {
        if (cause instanceof SerializationException) {
            log.error("Serialization error (non retryable)");
            return false;
        } else if (cause instanceof IllegalArgumentException) {
            log.error("Invalid argument (non retryable)");
            return false;
        } else if (cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted (non retryable)");
            return false;
        } else if (cause instanceof ExecutionException) {
            log.warn("Execution error (retryable)");
            return true;
        } else if (cause instanceof KafkaException) {
            log.warn("Kafka error (retryable)");
            return true;
        } else if (cause instanceof TimeoutException) {
            log.warn("⏳ Timeout (retryable)");
            return true;
        } else {
            log.warn("❓ Unknown error (retryable by default)");
            return true;
        }
    }
}
