package com.roudane.order.infra_order.adapter.output.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
