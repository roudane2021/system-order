package com.roudane.inventory.infra.persistence.mapper;

import com.roudane.inventory.infra.persistence.entity.OutboxEntity;
import com.roudane.transverse.model.OutboxModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersistenceOutBoxMapper {
    OutboxEntity toEntity(final OutboxModel outboxModel);
}
