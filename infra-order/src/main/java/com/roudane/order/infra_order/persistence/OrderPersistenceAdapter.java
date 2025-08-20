package com.roudane.order.infra_order.persistence;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.infra_order.persistence.entity.OrderEntity;
import com.roudane.order.infra_order.persistence.mapper.PersistenceOrderMapper;
import com.roudane.order.infra_order.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class OrderPersistenceAdapter implements IOrderPersistenceOutPort {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        OrderEntity orderEntity = PersistenceOrderMapper.INSTANCE.toEntity(orderModel);
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
        return PersistenceOrderMapper.INSTANCE.toModel(savedEntity);
    }

    @Override
    public Optional<OrderModel> findOrderById(Long orderID) {
        return orderJpaRepository.findById(orderID)
                .map(PersistenceOrderMapper.INSTANCE::toModel);
    }

    @Override
    public OrderModel getOrder(Long orderID) {
        return findOrderById(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderID)); // Consider a domain specific exception
    }

    @Override
    public Set<OrderModel> findAllOrders() {
        return orderJpaRepository.findAll().stream()
                .map(PersistenceOrderMapper.INSTANCE::toModel)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        if (orderModel.getId() == null || !orderJpaRepository.existsById(orderModel.getId())) {
            throw new RuntimeException("Order with ID " + orderModel.getId() + " not found, cannot update."); // Consider a domain specific exception
        }
        OrderEntity orderEntity = PersistenceOrderMapper.INSTANCE.toEntity(orderModel);
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity updatedEntity = orderJpaRepository.save(orderEntity);
        return PersistenceOrderMapper.INSTANCE.toModel(updatedEntity);
    }
}
