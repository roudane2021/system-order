package com.roudane.order.infra.persistence.mapper;


import com.roudane.order.domain_order.model.OutboxModel;
import com.roudane.order.infra.persistence.entity.OutboxEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PersistenceOutBoxMapper {

    OutboxEntity toEntity(final OutboxModel outboxModel);
}
