package com.roudane.inventory.infra.persistence.repository;

import com.roudane.inventory.infra.persistence.mapper.InventoryItemMapper;
import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import com.roudane.inventory.domain.repository.IInventoryRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; // Use @Component or @Repository
import java.util.List;
import java.util.Optional;

@Component // Or @Repository, functionally similar for this case
public class InventoryRepositoryAdapter implements IInventoryRepositoryPort {

    private final JpaInventoryItemRepository jpaRepository;
    private final InventoryItemMapper itemMapper;

    @Autowired
    public InventoryRepositoryAdapter(JpaInventoryItemRepository jpaRepository, InventoryItemMapper itemMapper) {
        this.jpaRepository = jpaRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        Optional<InventoryItemEntity> entityOptional = jpaRepository.findByProductId(productId);
        return entityOptional.map(itemMapper::toDomain);
    }

    @Override
    public InventoryItem save(InventoryItem inventoryItem) {
        InventoryItemEntity entity = itemMapper.toEntity(inventoryItem);
        InventoryItemEntity savedEntity = jpaRepository.save(entity);
        return itemMapper.toDomain(savedEntity);
    }

    @Override
    public List<InventoryItem> saveAll(List<InventoryItem> inventoryItems) {
        List<InventoryItemEntity> entities = itemMapper.toEntityList(inventoryItems);
        List<InventoryItemEntity> savedEntities = jpaRepository.saveAll(entities);
        return itemMapper.toDomainList(savedEntities);
    }

    @Override
    public List<InventoryItem> findByProductIdIn(List<String> productIds) {
        List<InventoryItemEntity> entities = jpaRepository.findByProductIdIn(productIds);
        return itemMapper.toDomainList(entities);
    }

    @Override
    public List<InventoryItem> findAll() {
        return itemMapper.toDomainList(jpaRepository.findAll());
    }
}
