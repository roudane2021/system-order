package com.roudane.inventory.infra.persistence.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {


    InventoryItemMapper INSTANCE = Mappers.getMapper(InventoryItemMapper.class);


    InventoryItemEntity toEntity(InventoryItem inventoryItem);

    InventoryItem toDomain(InventoryItemEntity inventoryItemEntity);

    List<InventoryItem> toDomainList(List<InventoryItemEntity> entities);

    List<InventoryItemEntity> toEntityList(List<InventoryItem> items);
}
