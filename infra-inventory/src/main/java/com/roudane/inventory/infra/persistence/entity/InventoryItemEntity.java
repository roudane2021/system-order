package com.roudane.inventory.infra.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "inventory_items",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id"})})
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_seq_gen")
    @SequenceGenerator(name = "inventory_seq_gen", sequenceName = "inventory_seq", allocationSize = 1)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private int quantity;

    // Default constructor for JPA
    public InventoryItemEntity() {}

    // Constructor for creating new entities
    public InventoryItemEntity(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString, equals, hashCode can be added if needed, typically good for entities
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItemEntity that = (InventoryItemEntity) o;
        return id != null && id.equals(that.id); // Entities are often compared by ID if persisted
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Or a hash based on ID if available
    }

    @Override
    public String toString() {
        return "InventoryItemEntity{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
