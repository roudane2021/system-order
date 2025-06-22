package com.roudane.order.infra_order.web.impl;


import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.service.OrderDomain;
import com.roudane.order.infra_order.web.IOrderController;
import com.roudane.order.infra_order.web.dto.*;
import com.roudane.order.infra_order.web.mapper.IOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements IOrderController {

    private final OrderDomain orderDomain;

    @PostMapping("/create") // Annotation was present, keep it
    @Override
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody final OrderCreateRequestDto orderCreateRequest) {
        OrderModel orderModelToCreate = IOrderMapper.INSTANCE.toModel(orderCreateRequest);
        OrderModel createdOrder = orderDomain.createOrder(orderModelToCreate);
        OrderCreateResponseDto responseDto = IOrderMapper.INSTANCE.toCreateResponseDto(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/retriever/{id}") // Annotation was present, keep it
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

    @PostMapping("/{id}/ship") // New endpoint
    @Override
    public ResponseEntity<OrderDto> shipOrder(@PathVariable final Long id, @RequestBody final ShipOrderRequestDto shipOrderRequestDto) {
        // Assuming OrderDomain implements IShipOrderUseCase
        OrderModel shippedOrderModel = orderDomain.shipOrder(id, shipOrderRequestDto.getTrackingNumber());
        final OrderDto orderDto = IOrderMapper.INSTANCE.toDto(shippedOrderModel);
        return ResponseEntity.ok(orderDto);
    }
}
