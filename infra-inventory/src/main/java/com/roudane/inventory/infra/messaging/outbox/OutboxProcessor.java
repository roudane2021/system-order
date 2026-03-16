package com.roudane.inventory.infra.messaging.outbox;

import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.port.output.json.IJsonOutPort;
import com.roudane.inventory.domain.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.inventory.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
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

    private final IInventoryEventPublisherOutPort inventoryEventPublisherOutPort;
    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final OutboxProperties outboxProperties;
    private final IJsonOutPort jsonOutPort;


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
            case INVENTORY_RESERVED -> {
                InventoryReservedEvent reserved = (InventoryReservedEvent) jsonOutPort.readValue((String) event.getPayload(), InventoryReservedEvent.class);
                inventoryEventPublisherOutPort.publish(reserved);
            }
            case INVENTORY_DEPLETED -> {
                InventoryDepletedEvent depleted = (InventoryDepletedEvent) jsonOutPort.readValue((String) event.getPayload(), InventoryDepletedEvent.class);
                inventoryEventPublisherOutPort.publish(depleted);
            }
        }
    }

    private void handleProcessingError(OutboxModel event, Exception ex) {
        Throwable cause = ex;

        while (cause.getCause() != null &&
                cause instanceof ExecutionException) {
            cause = cause.getCause();
        }

        boolean retryable = determineIfRetryable(cause);

        log.error("Outbox event {} failed : {}", event.getId(), cause.getMessage(), cause);

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
