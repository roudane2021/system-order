package com.roudane.inventory.infra.persistence.repository;

import com.roudane.inventory.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface JpaInventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductId(String productId);
    List<InventoryItem> findByProductIdIn(List<String> productIds);
}
