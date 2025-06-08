package com.roudane.order.domain_order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservedEvent {
    private Long orderId;
    private boolean reservationConfirmed; // Or an enum for more statuses
    // Add any other relevant fields, e.g., item-specific reservation details if needed
}
