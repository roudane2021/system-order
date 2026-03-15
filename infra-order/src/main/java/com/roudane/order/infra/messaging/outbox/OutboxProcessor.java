package com.roudane.order.infra.messaging.outbox;

import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.json.IJsonOutPort;
import com.roudane.order.domain_order.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import com.roudane.transverse.event.OrderShippedEvent;
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

    private final IOrderEventPublisherOutPort orderEventPublisherOutPort;
    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final IJsonOutPort jsonOutPort;
    private final OutboxProperties outboxProperties;


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

                    case "ORDER_CREATED":
                        OrderCreatedEvent created = (OrderCreatedEvent) jsonOutPort.readValue((String) event.getPayload(), OrderCreatedEvent.class);
                        orderEventPublisherOutPort.publishOrderCreatedEvent(created);
                        break;

                    case "ORDER_SHIPPED":
                        OrderShippedEvent shipped = (OrderShippedEvent) jsonOutPort.readValue((String) event.getPayload(), OrderShippedEvent.class);
                        orderEventPublisherOutPort.publishOrderShippedEvent(shipped);
                        break;

                    case "ORDER_CANCELLED":
                        OrderCancelledEvent cancelled = (OrderCancelledEvent) jsonOutPort.readValue((String) event.getPayload(), OrderCancelledEvent.class);
                        orderEventPublisherOutPort.publishOrderCancelledEvent(cancelled);
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
