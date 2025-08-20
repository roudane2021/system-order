package com.roudane.inventory.infra.persistence.repository;

import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {
    Optional<InventoryItemEntity> findByProductId(String productId);
    List<InventoryItemEntity> findByProductIdIn(List<String> productIds);
}
