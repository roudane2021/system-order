package com.roudane.order.infra_order.adapter.input.rest.order.impl;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra_order.adapter.input.rest.order.IOrderController;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
import com.roudane.order.infra_order.adapter.input.rest.order.mapper.IOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements IOrderController {
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
    @GetMapping
    @Override
    public ResponseEntity<OrderDto> retrieverOrders() {
        OrderModel orderModel = OrderModel.builder()
                .id(1L)
                .build();
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(orderModel);
        return ResponseEntity.ok(orderDto);
    }
}
