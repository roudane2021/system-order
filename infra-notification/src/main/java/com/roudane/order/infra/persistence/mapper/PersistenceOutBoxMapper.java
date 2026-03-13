package com.roudane.order.infra.persistence.mapper;

import com.roudane.order.infra.persistence.entity.OutboxEntity;
import com.roudane.transverse.model.OutboxModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersistenceOutBoxMapper {
    OutboxEntity toEntity(final OutboxModel outboxModel);
}
