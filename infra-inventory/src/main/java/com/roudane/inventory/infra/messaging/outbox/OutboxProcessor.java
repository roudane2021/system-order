package com.roudane.inventory.infra.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roudane.transverse.model.OutboxModel;
import com.roudane.inventory.domain.port.output.event.IInventoryEventPublisherOutPort;
import com.roudane.inventory.domain.port.output.json.IJsonOutPort;
import com.roudane.inventory.domain.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.inventory.infra.persistence.entity.OutboxEntity;
import com.roudane.inventory.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import com.roudane.transverse.event.enums.InventoryEventType;
import com.roudane.transverse.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        if (events.isEmpty()) {
            return;
        }

        log.debug("Processing {} pending outbox messages", events.size());

        for (OutboxModel event : events) {
            try {
                switch (event.getEventType()) {

                    case "INVENTORY_RESERVED":
                        InventoryReservedEvent reserved = (InventoryReservedEvent) jsonOutPort.readValue((String) event.getPayload(), InventoryReservedEvent.class);
                        inventoryEventPublisherOutPort.publish(reserved);
                        break;

                    case "INVENTORY_DEPLETED":
                        InventoryDepletedEvent depleted = (InventoryDepletedEvent) jsonOutPort.readValue((String) event.getPayload(), InventoryDepletedEvent.class);
                        inventoryEventPublisherOutPort.publish(depleted);
                        break;
                }

                outBoxPersistenceOutPort.markAsSent(event.getId());
                log.info("Successfully processed outbox message with ID: {}", event.getId());
            } catch (InternalErrorException e) {
                outBoxPersistenceOutPort.markAsError(event.getId(), e);
                log.error("Error processing outbox message with ID: {}", event.getId(), e);
            }
        }
    }
}
