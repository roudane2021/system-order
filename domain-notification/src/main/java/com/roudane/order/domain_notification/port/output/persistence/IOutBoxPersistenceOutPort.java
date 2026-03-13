package com.roudane.order.domain_notification.port.output.persistence;

import com.roudane.transverse.model.OutboxModel;

import java.util.List;

public interface IOutBoxPersistenceOutPort {
    void saveEvent(final OutboxModel outboxModel);
    List<OutboxModel> lockNextEvents(int limit, int maxRetries, int delay);
    void markAsSent(Long id);
    void markAsError(Long id, Exception e);
}
