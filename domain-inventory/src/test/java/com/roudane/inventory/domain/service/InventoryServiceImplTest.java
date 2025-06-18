package com.roudane.inventory.domain.service;

import com.roudane.inventory.domain.event.*;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private OrderCreatedEvent orderCreatedEvent;
    private OrderCancelledEvent orderCancelledEvent;
    private InventoryItem item1;
    private InventoryItem item2;

    @BeforeEach
    void setUp() {
        // Setup InventoryItems
        item1 = new InventoryItem("prod1", 10);
        item1.setId(1L); // Assuming IDs are set after creation or by DB
        item2 = new InventoryItem("prod2", 5);
        item2.setId(2L);

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
    void handleOrderCreated_sufficientInventory_shouldReturnInventoryReservedEvent() {
        // Arrange
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                                   .map(OrderItemDTO::getProductId)
                                                   .collect(Collectors.toList());
        when(inventoryRepository.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));
        when(inventoryRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Object result = inventoryService.handleOrderCreated(orderCreatedEvent);

        // Assert
        assertTrue(result instanceof InventoryReservedEvent);
        InventoryReservedEvent reservedEvent = (InventoryReservedEvent) result;
        assertEquals("order123", reservedEvent.getOrderId());
        assertEquals(2, reservedEvent.getItems().size());
        assertEquals(8, item1.getQuantity()); // 10 - 2
        assertEquals(2, item2.getQuantity()); // 5 - 3
        verify(inventoryRepository, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void handleOrderCreated_insufficientInventory_shouldReturnInventoryDepletedEvent() {
        // Arrange
        orderCreatedEvent.getItems().get(0).setQuantity(15); // Request 15 of prod1 (only 10 available)
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        when(inventoryRepository.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));

        // Act
        Object result = inventoryService.handleOrderCreated(orderCreatedEvent);

        // Assert
        assertTrue(result instanceof InventoryDepletedEvent);
        InventoryDepletedEvent depletedEvent = (InventoryDepletedEvent) result;
        assertEquals("order123", depletedEvent.getOrderId());
        assertTrue(depletedEvent.getReason().contains("Insufficient stock for productId: prod1"));
        assertEquals(10, item1.getQuantity()); // Quantity should not change
        assertEquals(5, item2.getQuantity());  // Quantity should not change
        verify(inventoryRepository, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepository, never()).saveAll(anyList());
    }

    @Test
    void handleOrderCreated_itemNotInInventory_shouldReturnInventoryDepletedEvent() {
        // Arrange
        orderCreatedEvent.getItems().add(new OrderItemDTO("prod3", 1)); // Request prod3 which is not in mock repo
        List<String> productIds = orderCreatedEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        // Simulate prod3 not being found by returning only item1 and item2
        when(inventoryRepository.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2));

        // Act
        Object result = inventoryService.handleOrderCreated(orderCreatedEvent);

        // Assert
        assertTrue(result instanceof InventoryDepletedEvent);
        InventoryDepletedEvent depletedEvent = (InventoryDepletedEvent) result;
        assertEquals("order123", depletedEvent.getOrderId());
        assertTrue(depletedEvent.getReason().contains("Insufficient stock for productId: prod3"));
         verify(inventoryRepository, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepository, never()).saveAll(anyList());
    }


    @Test
    void handleOrderCreated_emptyItemList_shouldReturnEmptyInventoryReservedEvent() {
        // Arrange
        OrderCreatedEvent emptyOrderEvent = new OrderCreatedEvent("emptyOrder", new ArrayList<>());

        // Act
        Object result = inventoryService.handleOrderCreated(emptyOrderEvent);

        // Assert
        assertTrue(result instanceof InventoryReservedEvent);
        InventoryReservedEvent reservedEvent = (InventoryReservedEvent) result;
        assertEquals("emptyOrder", reservedEvent.getOrderId());
        assertTrue(reservedEvent.getItems().isEmpty());
        verify(inventoryRepository, never()).findByProductIdIn(anyList());
        verify(inventoryRepository, never()).saveAll(anyList());
    }

    @Test
    void handleOrderCreated_nullItemList_shouldReturnEmptyInventoryReservedEvent() {
        // Arrange
        OrderCreatedEvent nullItemsOrderEvent = new OrderCreatedEvent("nullItemsOrder", null);

        // Act
        Object result = inventoryService.handleOrderCreated(nullItemsOrderEvent);

        // Assert
        assertTrue(result instanceof InventoryReservedEvent);
        InventoryReservedEvent reservedEvent = (InventoryReservedEvent) result;
        assertEquals("nullItemsOrder", reservedEvent.getOrderId());
        assertTrue(reservedEvent.getItems().isEmpty());
        verify(inventoryRepository, never()).findByProductIdIn(anyList());
        verify(inventoryRepository, never()).saveAll(anyList());
    }


    @Test
    void handleOrderCancelled_shouldIncrementInventory() {
        // Arrange
        List<String> productIds = orderCancelledEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());
        when(inventoryRepository.findByProductIdIn(productIds)).thenReturn(Arrays.asList(item1, item2)); // Initial state before cancellation
        when(inventoryRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));


        // Act
        inventoryService.handleOrderCancelled(orderCancelledEvent);

        // Assert
        assertEquals(11, item1.getQuantity()); // 10 + 1
        assertEquals(6, item2.getQuantity());  // 5 + 1
        verify(inventoryRepository, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void handleOrderCancelled_itemNotInInventory_shouldCreateNewItemWithReleasedQuantity() {
        // Arrange
        OrderItemDTO newItemDTO = new OrderItemDTO("prod3", 5);
        OrderCancelledEvent cancelWithNewItemEvent = new OrderCancelledEvent("order789", Arrays.asList(newItemDTO));

        List<String> productIds = cancelWithNewItemEvent.getItems().stream()
                                       .map(OrderItemDTO::getProductId)
                                       .collect(Collectors.toList());

        // Simulate prod3 not being in inventory initially
        when(inventoryRepository.findByProductIdIn(productIds)).thenReturn(new ArrayList<>());
        // Capture the argument to saveAll to check the new item
        when(inventoryRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<InventoryItem> savedItems = invocation.getArgument(0);
            // Simulate saving by returning the list. In a real scenario, the DB would generate an ID.
            // For this test, we'll check the state of the passed item.
            if (!savedItems.isEmpty() && "prod3".equals(savedItems.get(0).getProductId())) {
                // For test assertion, we can assign an ID if needed or just check quantity
                 savedItems.get(0).setId(3L);
            }
            return savedItems;
        });

        // Act
        inventoryService.handleOrderCancelled(cancelWithNewItemEvent);

        // Assert
        verify(inventoryRepository, times(1)).findByProductIdIn(productIds);
        verify(inventoryRepository, times(1)).saveAll(argThat(list ->
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
        verify(inventoryRepository, never()).findByProductIdIn(anyList());
        verify(inventoryRepository, never()).saveAll(anyList());
    }

    @Test
    void handleOrderCancelled_nullItemList_shouldDoNothing() {
        // Arrange
        OrderCancelledEvent nullItemsCancelEvent = new OrderCancelledEvent("nullItemsCancel", null);

        // Act
        inventoryService.handleOrderCancelled(nullItemsCancelEvent);

        // Assert
        verify(inventoryRepository, never()).findByProductIdIn(anyList());
        verify(inventoryRepository, never()).saveAll(anyList());
    }
}
