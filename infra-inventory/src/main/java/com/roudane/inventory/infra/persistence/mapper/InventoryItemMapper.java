package com.roudane.inventory.infra.persistence.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring") // Generates a Spring bean
public interface InventoryItemMapper {

    // Instance for manual use if needed, though Spring injection is preferred
    // InventoryItemMapper INSTANCE = Mappers.getMapper(InventoryItemMapper.class);

    // No specific @Mapping needed if field names are identical
    InventoryItemEntity toEntity(InventoryItem inventoryItem);

    InventoryItem toDomain(InventoryItemEntity inventoryItemEntity);

    List<InventoryItem> toDomainList(List<InventoryItemEntity> entities);

    List<InventoryItemEntity> toEntityList(List<InventoryItem> items);
}
