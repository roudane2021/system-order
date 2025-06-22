package com.roudane.inventory.infra.persistence.mapper;

import com.roudane.inventory.domain.model.InventoryItem;
import com.roudane.inventory.infra.persistence.entity.InventoryItemEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemMapperTest {

    private final InventoryItemMapper mapper = Mappers.getMapper(InventoryItemMapper.class);

    @Test
    void shouldMapEntityToDomain() {
        InventoryItemEntity entity = InventoryItemEntity.builder()
                .id(1L)
                .productId("prod123")
                .quantity(10)
                .build();


        InventoryItem domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("prod123", domain.getProductId());
        assertEquals(10, domain.getQuantity());
    }

    @Test
    void shouldMapDomainToEntity() {
        InventoryItem domain = new InventoryItem("prod456", 20);
        // Domain model no longer has setId for DB ID. Entity ID is set by DB or pre-persist.

        InventoryItemEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertNull(entity.getId()); // Mapper shouldn't set DB ID
        assertEquals("prod456", entity.getProductId());
        assertEquals(20, entity.getQuantity());
    }

    @Test
    void shouldMapNullEntityToNullDomain() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void shouldMapNullDomainToNullEntity() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldMapEntityListToDomainList() {
        InventoryItemEntity entity1 =  InventoryItemEntity.builder()
                .productId("prod5")
                .quantity(10)
                .build();
        InventoryItemEntity entity2 = InventoryItemEntity.builder()
                .productId("prod8")
                .quantity(8)
                .build();
        List<InventoryItemEntity> entities = Arrays.asList(entity1, entity2);

        List<InventoryItem> domains = mapper.toDomainList(entities);

        assertNotNull(domains);
        assertEquals(2, domains.size());
        assertEquals("prod1", domains.get(0).getProductId());
        assertEquals(5, domains.get(0).getQuantity());
    }

    @Test
    void shouldMapDomainListToEntityList() {
        InventoryItem domain1 = new InventoryItem("prodA", 15);
        InventoryItem domain2 = new InventoryItem("prodB", 18);
        List<InventoryItem> domains = Arrays.asList(domain1, domain2);

        List<InventoryItemEntity> entities = mapper.toEntityList(domains);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("prodA", entities.get(0).getProductId());
        assertEquals(15, entities.get(0).getQuantity());
    }
}
