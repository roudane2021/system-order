package com.roudane.order.infra_order.web.mapper;


import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra_order.web.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.web.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.web.dto.OrderDto;
import com.roudane.order.infra_order.web.dto.OrderUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// Removed 'uses' for now. MapStruct should auto-map items if fields are identical.
@Mapper(componentModel = "spring")
public interface IOrderMapper {

    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

    // From Model to DTO
    OrderDto toDto(final OrderModel orderModel); // Existing
    // @Mapping(source = "id", target = "id") // Example if explicit mapping is needed
    // @Mapping(source = "status", target = "status")
    OrderCreateResponseDto toCreateResponseDto(final OrderModel orderModel);

    // From DTO to Model
    // MapStruct should automatically map customerId and items if names match
    OrderModel toModel(final OrderCreateRequestDto orderCreateRequestDto);
    OrderModel toModel(final OrderUpdateRequestDto orderUpdateRequestDto);

    // Individual item mappers (if needed, MapStruct might handle it)
    // List<OrderItemModel> toOrderItemModelList(List<OrderItemDto> list);
    // List<OrderItemDto> toOrderItemDtoList(List<OrderItemModel> list);
    // OrderItemModel toModel(OrderItemDto orderItemDto);
    // OrderItemDto toDto(OrderItemModel orderItemModel);
}
