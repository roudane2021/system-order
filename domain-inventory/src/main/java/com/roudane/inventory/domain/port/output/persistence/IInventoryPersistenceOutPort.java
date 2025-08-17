package com.roudane.inventory.domain.port.output.persistence;

import com.roudane.inventory.domain.model.InventoryItem;
import java.util.Optional;
import java.util.List;

public interface IInventoryPersistenceOutPort {
    Optional<InventoryItem> findByProductId(String productId);
    InventoryItem save(InventoryItem inventoryItem);
    List<InventoryItem> saveAll(List<InventoryItem> inventoryItems);
    List<InventoryItem> findByProductIdIn(List<String> productIds);
    List<InventoryItem> findAll();
}
