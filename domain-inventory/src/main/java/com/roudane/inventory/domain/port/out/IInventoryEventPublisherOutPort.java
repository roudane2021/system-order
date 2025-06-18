package com.roudane.inventory.domain.port.out;

import com.roudane.inventory.domain.event.InventoryDepletedEvent;
import com.roudane.inventory.domain.event.InventoryReservedEvent;

/**
 * Output Port for publishing inventory-related events.
 */
public interface IInventoryEventPublisherOutPort {

    /**
     * Publishes an InventoryReservedEvent.
     *
     * @param event The InventoryReservedEvent to publish.
     */
    void publish(InventoryReservedEvent event);

    /**
     * Publishes an InventoryDepletedEvent.
     *
     * @param event The InventoryDepletedEvent to publish.
     */
    void publish(InventoryDepletedEvent event);

    // Add other event publishing methods if new domain events are created
}
