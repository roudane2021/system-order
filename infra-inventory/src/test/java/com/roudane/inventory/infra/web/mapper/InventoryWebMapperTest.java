package com.roudane.inventory.infra.web.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryWebMapperTest {

    private final InventoryWebMapper mapper = Mappers.getMapper(InventoryWebMapper.class);

    @Test
    void shouldMapDomainToDto() {
        InventoryItem domain = new InventoryItem("prod789", 30);

        InventoryItemDto dto = mapper.toDto(domain);

        assertNotNull(dto);
        assertEquals("prod789", dto.getProductId());
        assertEquals(30, dto.getQuantity());
    }

    @Test
    void shouldMapNullDomainToNullDto() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void shouldMapDomainListToDtoList() {
        InventoryItem domain1 = new InventoryItem("prodC", 25);
        InventoryItem domain2 = new InventoryItem("prodD", 38);
        List<InventoryItem> domains = Arrays.asList(domain1, domain2);

        List<InventoryItemDto> dtos = mapper.toDtoList(domains);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("prodC", dtos.get(0).getProductId());
        assertEquals(25, dtos.get(0).getQuantity());
    }
}
