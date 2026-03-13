package com.roudane.order.infra.messaging.outbox;

import com.roudane.order.domain_notification.port.output.event.INotificationEventPublisherOutPort;
import com.roudane.order.domain_notification.port.output.json.IJsonOutPort;
import com.roudane.order.domain_notification.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.infra.transverse.config.outbox.OutboxProperties;
import com.roudane.transverse.event.NotificationSentEvent;
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

    private final INotificationEventPublisherOutPort notificationEventPublisherOutPort;
    private final IOutBoxPersistenceOutPort outBoxPersistenceOutPort;
    private final IJsonOutPort jsonOutPort;
    private final OutboxProperties outboxProperties;

    @Scheduled(fixedDelayString = "${outbox.polling-delay}")
    public void processOutboxMessages() {
        List<OutboxModel> events = outBoxPersistenceOutPort.lockNextEvents(outboxProperties.getLimit(), outboxProperties.getMaxRetries(), outboxProperties.getDelaySeconds());

        if (events.isEmpty()) {
            return;
        }

        log.debug("Processing {} pending notification outbox messages", events.size());

        for (OutboxModel event : events) {
            try {
                switch (event.getEventType()) {
                    case NOTIFICATION_SENT:
                        NotificationSentEvent sentEvent = jsonOutPort.readValue(event.getPayload(), NotificationSentEvent.class);
                        notificationEventPublisherOutPort.publish(sentEvent);
                        break;
                    default:
                        log.warn("Unknown event type for notification outbox: {}", event.getEventType());
                }

                outBoxPersistenceOutPort.markAsSent(event.getId());
                log.info("Successfully processed notification outbox message with ID: {}", event.getId());
            } catch (Exception e) {
                outBoxPersistenceOutPort.markAsError(event.getId(), e);
                log.error("Error processing notification outbox message with ID: {}", event.getId(), e);
            }
        }
    }
}
