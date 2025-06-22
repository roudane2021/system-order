package com.roudane.order.infra_order.persistence.repository;

import com.roudane.order.infra_order.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    // Custom query methods can be added here if needed
}
