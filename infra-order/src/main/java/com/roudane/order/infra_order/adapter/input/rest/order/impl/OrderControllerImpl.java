package com.roudane.order.infra_order.adapter.input.rest.order.impl;

package com.roudane.order.infra_order.adapter.input.rest.order.impl;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.service.OrderDomain;
import com.roudane.order.infra_order.adapter.input.rest.order.IOrderController;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateResponseDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderUpdateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.mapper.IOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // Added for ResponseEntity.status()
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Added for PutMapping
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set; // Added for Set
import java.util.stream.Collectors; // Added for stream operations

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements IOrderController {

    private final OrderDomain orderDomain;
    // private final IOrderMapper orderMapper; // Mapper should be injected if componentModel="spring"

    @PostMapping("/create") // Annotation was present, keep it
    @Override
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody final OrderCreateRequestDto orderCreateRequest) {
        OrderModel orderModelToCreate = IOrderMapper.INSTANCE.toModel(orderCreateRequest);
        OrderModel createdOrder = orderDomain.createOrder(orderModelToCreate);
        OrderCreateResponseDto responseDto = IOrderMapper.INSTANCE.toCreateResponseDto(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}") // Annotation was present, keep it
    @Override
    public ResponseEntity<OrderDto> retrieverOrders(@PathVariable final Long id) {
        OrderModel orderModel = orderDomain.getOrder(id);
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(orderModel);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/all") // New endpoint
    @Override
    public ResponseEntity<Set<OrderDto>> listOrders() {
        Set<OrderModel> orderModels = orderDomain.listOrder();
        Set<OrderDto> orderDtos = orderModels.stream()
                .map(IOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(orderDtos);
    }

    @PutMapping("/{id}") // New endpoint
    @Override
    public ResponseEntity<OrderDto> updateOrder(@PathVariable final Long id, @RequestBody final OrderUpdateRequestDto orderUpdateRequestDto) {
        OrderModel orderModelToUpdate = IOrderMapper.INSTANCE.toModel(orderUpdateRequestDto);
        orderModelToUpdate.setId(id); // Set the ID from path variable

        OrderModel updatedOrder = orderDomain.updateOrder(orderModelToUpdate);
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{id}/cancel") // New endpoint
    @Override
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable final Long id) {
        OrderModel cancelledOrder = orderDomain.cancelOrder(id);
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(cancelledOrder);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{id}/pay") // New endpoint
    @Override
    public ResponseEntity<OrderDto> payOrder(@PathVariable final Long id) {
        OrderModel paidOrder = orderDomain.payOrder(id);
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(paidOrder);
        return ResponseEntity.ok(orderDto);
    }
}
