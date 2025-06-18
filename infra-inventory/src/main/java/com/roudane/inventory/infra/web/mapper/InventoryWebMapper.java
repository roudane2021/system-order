package com.roudane.inventory.infra.web.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryWebMapper {

    InventoryItemDto toDto(InventoryItem inventoryItem);

    // If we had a DTO to create/update InventoryItem domain object:
    // InventoryItem toDomain(InventoryItemDto inventoryItemDto);

    List<InventoryItemDto> toDtoList(List<InventoryItem> inventoryItems);
}
