package com.roudane.order.infra_order.persistence.mapper;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra_order.persistence.entity.OrderEntity;
import com.roudane.order.infra_order.persistence.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersistenceOrderMapper {

    PersistenceOrderMapper INSTANCE = Mappers.getMapper(PersistenceOrderMapper.class);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toEntity(OrderItemModel orderItemModel);
    List<OrderItemEntity> toEntityList(List<OrderItemModel> orderItemModels);


    OrderEntity toEntity(OrderModel orderModel);

    // OrderEntity to OrderModel
    @Mapping(target = "orderId", source = "order.id")
    OrderItemModel toModel(OrderItemEntity orderItemEntity);
    List<OrderItemModel> toModelList(List<OrderItemEntity> orderItemEntities);

    OrderModel toModel(OrderEntity orderEntity);
}
