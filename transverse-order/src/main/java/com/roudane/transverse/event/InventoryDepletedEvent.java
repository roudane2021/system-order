package com.roudane.transverse.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDepletedEvent {
    private Long orderId;
    private String reason;
    private List<OrderItemEvent> requestedItems;

}
