package com.roudane.order.infra_order.web;

// Assuming an OrderUpdateRequestDto will be created later for the update operation

import com.roudane.order.infra_order.web.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IOrderController {

    ResponseEntity<OrderCreateResponseDto> createOrder(final OrderCreateRequestDto orderCreateRequest);

    ResponseEntity<OrderDto> retrieverOrders(final Long id);

    ResponseEntity<Set<OrderDto>> listOrders();

    ResponseEntity<OrderDto> updateOrder(final Long id, final OrderUpdateRequestDto orderUpdateRequestDto);

    ResponseEntity<OrderDto> cancelOrder(final Long id);

    ResponseEntity<OrderDto> payOrder(final Long id);

    ResponseEntity<OrderDto> shipOrder(Long id, ShipOrderRequestDto shipOrderRequestDto); // New DTO needed
}
