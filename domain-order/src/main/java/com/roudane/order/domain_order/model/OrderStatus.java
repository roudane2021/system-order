package com.roudane.order.domain_order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum OrderStatus {
    CREATED,
    CANCELLED,
    SHIPPED
}
