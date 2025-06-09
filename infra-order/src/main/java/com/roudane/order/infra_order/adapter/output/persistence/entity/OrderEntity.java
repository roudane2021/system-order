package com.roudane.order.infra_order.adapter.output.persistence.entity;

import com.roudane.order.domain_order.model.OrderStatus; // Assuming this enum can be used directly

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode; // Import for Exclude
import lombok.NoArgsConstructor;
import lombok.ToString; // Import for Exclude

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", allocationSize = 1)
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Add this
    @EqualsAndHashCode.Exclude // Add this
    private List<OrderItemEntity> items;
}
