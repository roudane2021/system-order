package com.roudane.order.infra_order.adapter.output.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // private Long orderId; // Replaced by ManyToOne relationship
    private Long productId;
    private int quantity;
    private BigDecimal price;

    @ManyToOne
    // @JoinColumn(name = "order_id") // MapStruct might need consistent naming or explicit mapping if this is different from OrderEntity's mappedBy
    private OrderEntity order;
}
