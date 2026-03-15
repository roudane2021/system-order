package com.roudane.inventory.infra.persistence.mapper;


import com.roudane.transverse.model.OutboxModel;
import com.roudane.inventory.infra.persistence.entity.OutboxEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PersistenceOutBoxMapper {

    OutboxEntity toEntity(final OutboxModel outboxModel);
}
