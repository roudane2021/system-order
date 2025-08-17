package com.roudane.inventory.infra.web.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryWebMapper {

    InventoryWebMapper INSTANCE = Mappers.getMapper(InventoryWebMapper.class);

    InventoryItemDto toDto(InventoryItem inventoryItem);
    List<InventoryItemDto> toDtoList(List<InventoryItem> inventoryItems);
}
