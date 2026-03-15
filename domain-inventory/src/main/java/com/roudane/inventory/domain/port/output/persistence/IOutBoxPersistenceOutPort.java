package com.roudane.inventory.domain.port.output.persistence;

import com.roudane.transverse.model.OutboxModel;
import com.roudane.transverse.enums.OutboxStatus;

import java.util.List;

public interface IOutBoxPersistenceOutPort {

    void saveEvent(final OutboxModel outboxModel);
    List<OutboxModel> lockNextEvents(int limit,  int maxRetries, int delay);
    void markAsSent(Long id);
    void markAsError(Long id, Exception e);
}
