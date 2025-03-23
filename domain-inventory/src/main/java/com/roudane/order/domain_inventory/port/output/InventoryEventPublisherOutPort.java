package com.roudane.order.domain_inventory.port.output;

import com.roudane.order.domain_inventory.event.InventoryEvent;

public interface InventoryEventPublisherOutPort {
    void publisherEventOrder(final InventoryEvent orderEvent);


}
