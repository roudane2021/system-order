package com.roudane.order.infra_order.adapter.input.rest.order.mapper;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IOrderMapper {

    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);
    OrderDto toDto(final OrderModel orderModel);
}
