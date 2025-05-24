package com.roudane.order.infra_order.adapter.input.rest.order;

import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IOrderController {

    ResponseEntity<OrderCreateResponseDto> createOrder(final OrderCreateRequestDto orderCreateRequest);
    ResponseEntity<OrderDto> retrieverOrders(final Long id);
}
