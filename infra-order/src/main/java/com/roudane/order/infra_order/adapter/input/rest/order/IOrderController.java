package com.roudane.order.infra_order.adapter.input.rest.order;

import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
// Assuming an OrderUpdateRequestDto will be created later for the update operation
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderUpdateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IOrderController {

    ResponseEntity<OrderCreateResponseDto> createOrder(final OrderCreateRequestDto orderCreateRequest);

    ResponseEntity<OrderDto> retrieverOrders(final Long id);

    ResponseEntity<Set<OrderDto>> listOrders();

    ResponseEntity<OrderDto> updateOrder(final Long id, final OrderUpdateRequestDto orderUpdateRequestDto);

    ResponseEntity<OrderDto> cancelOrder(final Long id);

    ResponseEntity<OrderDto> payOrder(final Long id);
}
