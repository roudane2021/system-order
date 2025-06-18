package com.roudane.inventory.infra.persistence.repository;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.repository.IInventoryRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; // Use @Component or @Repository
import java.util.List;
import java.util.Optional;

@Component // Or @Repository, functionally similar for this case
public class InventoryRepositoryAdapter implements IInventoryRepositoryPort {

    private final JpaInventoryItemRepository jpaRepository;

    @Autowired
    public InventoryRepositoryAdapter(JpaInventoryItemRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId);
    }

    @Override
    public InventoryItem save(InventoryItem inventoryItem) {
        return jpaRepository.save(inventoryItem);
    }

    @Override
    public List<InventoryItem> saveAll(List<InventoryItem> inventoryItems) {
        return jpaRepository.saveAll(inventoryItems);
    }

    @Override
    public List<InventoryItem> findByProductIdIn(List<String> productIds) {
        return jpaRepository.findByProductIdIn(productIds);
    }
}
