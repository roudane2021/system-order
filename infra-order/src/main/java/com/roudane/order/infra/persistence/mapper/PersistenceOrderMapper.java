package com.roudane.order.infra.persistence.mapper;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra.persistence.entity.OrderEntity;
import com.roudane.order.infra.persistence.entity.OrderItemEntity;
import com.roudane.transverse.module.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

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
    List<OrderModel> toOrderModelList(List<OrderEntity> orderItemEntities);

    default PageResult<OrderModel> toPageResult(Page<OrderEntity> entityPage) {
        return PageResult.<OrderModel>builder()
                .content(toOrderModelList(entityPage.getContent()))
                .number(entityPage.getNumber())
                .size(entityPage.getSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .build();
    }
}
