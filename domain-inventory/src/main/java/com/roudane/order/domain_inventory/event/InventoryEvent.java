package com.roudane.order.domain_inventory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryEvent {
    private String orderId;
    private Set<String> itemIds;
    private boolean success;
    private LocalDateTime dateEvent;
    private String itemId;
    private InventoryEventType eventType;

}
