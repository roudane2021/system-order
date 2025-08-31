package com.roudane.order.infra.web;



import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.infra.web.dto.*;
import com.roudane.transverse.criteria.CriteriaApplication;
import com.roudane.transverse.module.PageResult;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface IOrderController {

    ResponseEntity<OrderCreateResponseDto> createOrder(final OrderCreateRequestDto orderCreateRequest);

    ResponseEntity<OrderDto> retrieverOrders(final Long id);

    ResponseEntity<Set<OrderDto>> listOrders();

    ResponseEntity<PageResult<OrderDto>> listOrders(final List<CriteriaApplication> criteriaApplications, final int page, final int size);

    ResponseEntity<OrderDto> updateOrder(final Long id, final OrderUpdateRequestDto orderUpdateRequestDto);

    ResponseEntity<OrderDto> cancelOrder(final Long id);

    ResponseEntity<OrderDto> payOrder(final Long id);

    ResponseEntity<OrderDto> shipOrder(Long id, ShipOrderRequestDto shipOrderRequestDto); // New DTO needed
}
