package com.roudane.inventory.domain.repository;

import com.roudane.inventory.domain.model.InventoryItem;
import java.util.Optional;
import java.util.List;

public interface IInventoryRepositoryPort {
    Optional<InventoryItem> findByProductId(String productId);
    InventoryItem save(InventoryItem inventoryItem);
    List<InventoryItem> saveAll(List<InventoryItem> inventoryItems);
    // May need methods to find multiple products at once for efficiency
    List<InventoryItem> findByProductIdIn(List<String> productIds);
}
