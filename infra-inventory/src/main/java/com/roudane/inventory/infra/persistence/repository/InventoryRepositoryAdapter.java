package com.roudane.inventory.infra.persistence.repository;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.domain.port.output.persistence.IInventoryPersistenceOutPort;
import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import com.roudane.inventory.infra.persistence.mapper.InventoryItemMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter implements IInventoryPersistenceOutPort {

    private final JpaInventoryItemRepository jpaRepository;



    public InventoryRepositoryAdapter(JpaInventoryItemRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        Optional<InventoryItemEntity> entityOptional = jpaRepository.findByProductId(productId);
        return entityOptional.map(InventoryItemMapper.INSTANCE::toDomain);
    }

    @Override
    public InventoryItem save(InventoryItem inventoryItem) {
        InventoryItemEntity entity = InventoryItemMapper.INSTANCE.toEntity(inventoryItem);
        InventoryItemEntity savedEntity = jpaRepository.save(entity);
        return InventoryItemMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public List<InventoryItem> saveAll(List<InventoryItem> inventoryItems) {
        List<InventoryItemEntity> entities = InventoryItemMapper.INSTANCE.toEntityList(inventoryItems);
        List<InventoryItemEntity> savedEntities = jpaRepository.saveAll(entities);
        return InventoryItemMapper.INSTANCE.toDomainList(savedEntities);
    }

    @Override
    public List<InventoryItem> findByProductIdIn(List<String> productIds) {
        List<InventoryItemEntity> entities = jpaRepository.findByProductIdIn(productIds);
        return InventoryItemMapper.INSTANCE.toDomainList(entities);
    }

    @Override
    public List<InventoryItem> findAll() {
        return InventoryItemMapper.INSTANCE.toDomainList(jpaRepository.findAll());
    }
}
