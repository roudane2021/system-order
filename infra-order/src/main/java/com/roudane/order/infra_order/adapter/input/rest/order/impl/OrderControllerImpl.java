package com.roudane.order.infra_order.adapter.input.rest.order.impl;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.service.OrderDomain;
import com.roudane.order.infra_order.adapter.input.rest.order.IOrderController;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
import com.roudane.order.infra_order.adapter.input.rest.order.mapper.IOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements IOrderController {

    private final OrderDomain orderDomain;
    /**
     * @param orderCreateRequest
     * @return
     */
    @PostMapping("/create")
    @Override
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequestDto orderCreateRequest) {
        return null;
    }

    /**
     * @return
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<OrderDto> retrieverOrders(@PathVariable final Long id) {
        OrderModel orderModel = orderDomain.getOrder(id);
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(orderModel);
        return ResponseEntity.ok(orderDto);
    }
}
