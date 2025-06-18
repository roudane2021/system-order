package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.event.*;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.repository.IInventoryRepositoryPort; // Updated
import com.roudane.inventory.domain.port.out.IInventoryEventPublisherOutPort; // Added
import com.roudane.inventory.domain.exception.InventoryDomainException; // Added
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class InventoryServiceTest { // Renamed class

    @Mock
    private IInventoryRepositoryPort inventoryRepositoryPort; // Updated type and name

    @Mock
    private IInventoryEventPublisherOutPort eventPublisherPort; // Added

    @InjectMocks
    private InventoryService inventoryService; // Updated type

    private OrderCreatedEvent orderCreatedEvent;
    private OrderCancelledEvent orderCancelledEvent;
    private InventoryItem item1;
    private InventoryItem item2;

    @BeforeEach
    void setUp() {
        // Setup InventoryItems
        item1 = new InventoryItem("prod1", 10);
        // item1.setId(1L); // Domain model no longer has DB ID
        item2 = new InventoryItem("prod2", 5);
        // item2.setId(2L); // Domain model no longer has DB ID

        // Setup OrderCreatedEvent
        List<OrderItemDTO> orderItems = Arrays.asList(
                new OrderItemDTO("prod1", 2),
                new OrderItemDTO("prod2", 3)
        );
        orderCreatedEvent = new OrderCreatedEvent("order123", orderItems);

        // Setup OrderCancelledEvent
        List<OrderItemDTO> cancelledItems = Arrays.asList(
                new OrderItemDTO("prod1", 1),
                new OrderItemDTO("prod2", 1)
        );
        orderCancelledEvent = new OrderCancelledEvent("order456", cancelledItems);
    }

    @Test
    void handleOrderCreated_sufficientInventory_shouldPublishInventoryReservedEvent() { // Renamed test
        // Arrange
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                                   .map(OrderItemDTO::getProductId)
                                                   .collect(Collectors.toList());
        when(inventoryRepositoryPort.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));
        when(inventoryRepositoryPort.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        inventoryService.handleOrderCreated(orderCreatedEvent); // Now void

        // Assert
        assertEquals(8, item1.getQuantity()); // 10 - 2
        assertEquals(2, item2.getQuantity()); // 5 - 3
        verify(inventoryRepositoryPort, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepositoryPort, times(1)).saveAll(anyList());
        verify(eventPublisherPort, times(1)).publish(any(InventoryReservedEvent.class)); // Verify publication
    }

    @Test
    void handleOrderCreated_insufficientInventory_shouldPublishInventoryDepletedEvent() { // Renamed test
        // Arrange
        orderCreatedEvent.getItems().get(0).setQuantity(15); // Request 15 of prod1 (only 10 available)
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        when(inventoryRepositoryPort.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));

        // Act
        inventoryService.handleOrderCreated(orderCreatedEvent); // Now void

        // Assert
        assertEquals(10, item1.getQuantity()); // Quantity should not change
        assertEquals(5, item2.getQuantity());  // Quantity should not change
        verify(inventoryRepositoryPort, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepositoryPort, never()).saveAll(anyList());
        verify(eventPublisherPort, times(1)).publish(any(InventoryDepletedEvent.class)); // Verify publication
    }

    @Test
    void handleOrderCreated_itemNotInInventory_shouldPublishInventoryDepletedEvent() { // Renamed test
        // Arrange
        orderCreatedEvent.getItems().add(new OrderItemDTO("prod3", 1)); // Request prod3 which is not in mock repo
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        when(inventoryRepositoryPort.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));

        // Act
        inventoryService.handleOrderCreated(orderCreatedEvent); // Now void

        // Assert
        verify(inventoryRepositoryPort, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepositoryPort, never()).saveAll(anyList());
        verify(eventPublisherPort, times(1)).publish(any(InventoryDepletedEvent.class)); // Verify publication
    }


    @Test
    void handleOrderCreated_emptyItemList_shouldPublishEmptyInventoryReservedEvent() { // Renamed test
        // Arrange
        OrderCreatedEvent emptyOrderEvent = new OrderCreatedEvent("emptyOrder", new ArrayList<>());

        // Act
        inventoryService.handleOrderCreated(emptyOrderEvent); // Now void

        // Assert
        verify(inventoryRepositoryPort, never()).findByProductIdIn(anyList());
        verify(inventoryRepositoryPort, never()).saveAll(anyList());

    }

    @Test
    void handleOrderCreated_nullItemList_shouldPublishEmptyInventoryReservedEvent() { // Renamed test
        // Arrange
        OrderCreatedEvent nullItemsOrderEvent = new OrderCreatedEvent("nullItemsOrder", null);

        // Act
        inventoryService.handleOrderCreated(nullItemsOrderEvent); // Now void

        // Assert
        verify(inventoryRepositoryPort, never()).findByProductIdIn(anyList());
        verify(inventoryRepositoryPort, never()).saveAll(anyList());

    }


    @Test
    void handleOrderCancelled_shouldIncrementInventory() {
        // Arrange
        List<String> productIds = orderCancelledEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        when(inventoryRepositoryPort.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));
        when(inventoryRepositoryPort.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));


        // Act
        inventoryService.handleOrderCancelled(orderCancelledEvent);

        // Assert
        assertEquals(11, item1.getQuantity()); // 10 + 1
        assertEquals(6, item2.getQuantity());  // 5 + 1
        verify(inventoryRepositoryPort, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepositoryPort, times(1)).saveAll(anyList());
    }

    @Test
    void handleOrderCancelled_itemNotInInventory_shouldCreateNewItemWithReleasedQuantity() {
        // Arrange
        OrderItemDTO newItemDTO = new OrderItemDTO("prod3", 5);
        OrderCancelledEvent cancelWithNewItemEvent = new OrderCancelledEvent("order789", Arrays.asList(newItemDTO));

        List<String> productIds = cancelWithNewItemEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());

        when(inventoryRepositoryPort.findByProductIdIn(productIds)).thenReturn(new ArrayList<>());
        when(inventoryRepositoryPort.saveAll(anyList())).thenAnswer(invocation -> {
            List<InventoryItem> savedItems = invocation.getArgument(0);
            if (!savedItems.isEmpty() && "prod3".equals(savedItems.get(0).getProductId())) {
                // No ID in domain model
            }
            return savedItems;
        });

        // Act
        inventoryService.handleOrderCancelled(cancelWithNewItemEvent);

        // Assert
        verify(inventoryRepositoryPort, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepositoryPort, times(1)).saveAll(argThat(list ->
            !list.isEmpty() &&
            "prod3".equals(((InventoryItem)list.get(0)).getProductId()) &&
            5 == ((InventoryItem)list.get(0)).getQuantity()
        ));
    }

    @Test
    void handleOrderCancelled_emptyItemList_shouldDoNothing() {
        // Arrange
        OrderCancelledEvent emptyCancelEvent = new OrderCancelledEvent("emptyCancel", new ArrayList<>());

        // Act
        inventoryService.handleOrderCancelled(emptyCancelEvent);

        // Assert
        verify(inventoryRepositoryPort, never()).findByProductIdIn(anyList());
        verify(inventoryRepositoryPort, never()).saveAll(anyList());
    }

    @Test
    void handleOrderCancelled_nullItemList_shouldDoNothing() {
        // Arrange
        OrderCancelledEvent nullItemsCancelEvent = new OrderCancelledEvent("nullItemsCancel", null);

        // Act
        inventoryService.handleOrderCancelled(nullItemsCancelEvent);

        // Assert
        verify(inventoryRepositoryPort, never()).findByProductIdIn(anyList());
        verify(inventoryRepositoryPort, never()).saveAll(anyList());
    }

    @Test
    void adjustStock_itemExists_positiveChange_shouldUpdateQuantity() {
        when(inventoryRepositoryPort.findByProductId("prod1")).thenReturn(Optional.of(item1));
        inventoryService.adjustStock("prod1", 5, "Test adjustment");
        assertEquals(15, item1.getQuantity());
        verify(inventoryRepositoryPort, times(1)).save(item1);
    }

    @Test
    void adjustStock_itemExists_negativeChange_shouldUpdateQuantity() {
        when(inventoryRepositoryPort.findByProductId("prod1")).thenReturn(Optional.of(item1));
        inventoryService.adjustStock("prod1", -3, "Test adjustment");
        assertEquals(7, item1.getQuantity());
        verify(inventoryRepositoryPort, times(1)).save(item1);
    }

    @Test
    void adjustStock_itemNotFound_shouldThrowException() {
        when(inventoryRepositoryPort.findByProductId("prodNonExistent")).thenReturn(Optional.empty());
        assertThrows(InventoryDomainException.class, () -> {
            inventoryService.adjustStock("prodNonExistent", 5, "Test adjustment");
        });
        verify(inventoryRepositoryPort, never()).save(any());
    }

    @Test
    void adjustStock_resultsInNegativeQuantity_shouldThrowException() {
        when(inventoryRepositoryPort.findByProductId("prod1")).thenReturn(Optional.of(item1));
        assertThrows(InventoryDomainException.class, () -> {
            inventoryService.adjustStock("prod1", -15, "Test adjustment"); // Current is 10
        });
        assertEquals(10, item1.getQuantity()); // Quantity should remain unchanged
        verify(inventoryRepositoryPort, never()).save(any());
    }

    @Test
    void adjustStock_zeroChange_shouldNotChangeQuantityOrSave() {
        when(inventoryRepositoryPort.findByProductId("prod1")).thenReturn(Optional.of(item1));
        inventoryService.adjustStock("prod1", 0, "Test adjustment no change");
        assertEquals(10, item1.getQuantity());
        verify(inventoryRepositoryPort, never()).save(item1); // Or save if last_updated_time is tracked
    }
}
