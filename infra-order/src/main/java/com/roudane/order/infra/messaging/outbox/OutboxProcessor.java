package com.roudane.order.infra.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.roudane.order.domain_order.model.OutboxModel;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.infra.persistence.OrderPersistenceAdapter;
import com.roudane.transverse.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OrderPersistenceAdapter orderPersistenceAdapter;
    private final IOrderEventPublisherOutPort orderEventPublisherOutPort;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Transactional
    @Scheduled(fixedDelayString = "${outbox.polling-delay:5000}")
    public void processOutboxMessages() {
        List<OutboxModel> pendingMessages = orderPersistenceAdapter.findPendingOutboxMessages();

        if (pendingMessages.isEmpty()) {
            return;
        }

        log.debug("Processing {} pending outbox messages", pendingMessages.size());

        for (OutboxModel message : pendingMessages) {
            try {
                if ("OrderCreatedEvent".equals(message.getEventType())) {
                    OrderCreatedEvent event = objectMapper.readValue((String) message.getPayload(), OrderCreatedEvent.class);
                    orderEventPublisherOutPort.publishOrderCreatedEvent(event);
                } else {
                    log.warn("Unknown event type in outbox: {}", message.getEventType());
                }

                orderPersistenceAdapter.markOutboxAsProcessed(message.getId());
                log.info("Successfully processed outbox message with ID: {}", message.getId());
            } catch (Exception e) {
                log.error("Error processing outbox message with ID: {}", message.getId(), e);
            }
        }
    }
}
