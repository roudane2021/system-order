package com.roudane.inventory.domain.port.output.event;


import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;

import java.util.concurrent.ExecutionException;

/**
 * Output Port for publishing inventory-related events.
 */
public interface IInventoryEventPublisherOutPort {

    /**
     * Publishes an InventoryReservedEvent.
     *
     * @param event The InventoryReservedEvent to publish.
     */
    void publish(InventoryReservedEvent event) throws ExecutionException, InterruptedException;

    /**
     * Publishes an InventoryDepletedEvent.
     *
     * @param event The InventoryDepletedEvent to publish.
     */
    void publish(InventoryDepletedEvent event) throws ExecutionException, InterruptedException;

}
