package com.roudane.inventory.domain.port.output.event;


import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;

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

}
