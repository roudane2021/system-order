package com.roudane.order.infra_order.adapter.output.persistence.mapper;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra_order.adapter.output.persistence.entity.OrderEntity;
import com.roudane.order.infra_order.adapter.output.persistence.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersistenceOrderMapper {

    PersistenceOrderMapper INSTANCE = Mappers.getMapper(PersistenceOrderMapper.class);

    // OrderModel to OrderEntity
    @Mapping(target = "order", ignore = true) // Avoid cycle in OrderItemEntity mapping if order is present there
    OrderItemEntity toEntity(OrderItemModel orderItemModel);
    List<OrderItemEntity> toEntityList(List<OrderItemModel> orderItemModels);

    // @Mapping(target = "items", qualifiedByName = "toEntityList") // if explicit qualification needed
    OrderEntity toEntity(OrderModel orderModel);

    // OrderEntity to OrderModel
    @Mapping(target = "orderId", source = "order.id") // Map order.id to orderId in OrderItemModel
    OrderItemModel toModel(OrderItemEntity orderItemEntity);
    List<OrderItemModel> toModelList(List<OrderItemEntity> orderItemEntities);

    OrderModel toModel(OrderEntity orderEntity);
}
