package com.roudane.inventory.infra.messaging.outbox;

import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.port.output.json.IJsonOutPort;
import com.roudane.inventory.domain.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.inventory.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import com.roudane.transverse.exception.InternalErrorException;
import com.roudane.transverse.model.OutboxModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final IInventoryEventPublisherOutPort inventoryEventPublisherOutPort;
    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final IJsonOutPort jsonOutPort;
    private final OutboxProperties outboxProperties;

    @Scheduled(fixedDelayString = "${outbox.polling-delay}")
    public void processOutboxMessages() {
        List<OutboxModel> events = outBoxPersistenceOutPort.lockNextEvents(outboxProperties.getLimit(), outboxProperties.getMaxRetries(), outboxProperties.getDelaySeconds());

        if (events.isEmpty()) {
            return;
        }

        log.debug("Processing {} pending inventory outbox messages", events.size());

        for (OutboxModel event : events) {
            try {
                switch (event.getEventType()) {
                    case INVENTORY_RESERVED:
                        InventoryReservedEvent reserved = jsonOutPort.readValue(event.getPayload(), InventoryReservedEvent.class);
                        inventoryEventPublisherOutPort.publish(reserved);
                        break;
                    case INVENTORY_DEPLETED:
                        InventoryDepletedEvent depleted = jsonOutPort.readValue(event.getPayload(), InventoryDepletedEvent.class);
                        inventoryEventPublisherOutPort.publish(depleted);
                        break;
                    default:
                        log.warn("Unknown event type for inventory outbox: {}", event.getEventType());
                }

                outBoxPersistenceOutPort.markAsSent(event.getId());
                log.info("Successfully processed inventory outbox message with ID: {}", event.getId());
            } catch (Exception e) {
                outBoxPersistenceOutPort.markAsError(event.getId(), e);
                log.error("Error processing inventory outbox message with ID: {}", event.getId(), e);
            }
        }
    }
}
