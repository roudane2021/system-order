package com.roudane.order.infra_order.adapter.output.persistence;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.infra_order.adapter.output.persistence.entity.OrderEntity;
import com.roudane.order.infra_order.adapter.output.persistence.mapper.PersistenceOrderMapper;
import com.roudane.order.infra_order.adapter.output.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("oracle")
@RequiredArgsConstructor
@Transactional // Add transactional behavior to the adapter
public class OracleOrderPersistenceAdapter implements IOrderPersistenceOutPort {

    private final OrderJpaRepository orderJpaRepository;
    private final PersistenceOrderMapper persistenceOrderMapper;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        OrderEntity orderEntity = persistenceOrderMapper.toEntity(orderModel);
        // Ensure items in OrderEntity have their 'order' field set for cascading persistence
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
        return persistenceOrderMapper.toModel(savedEntity);
    }

    @Override
    public Optional<OrderModel> findOrderById(Long orderID) {
        return orderJpaRepository.findById(orderID)
                .map(persistenceOrderMapper::toModel);
    }

    @Override
    public OrderModel getOrder(Long orderID) {
        return findOrderById(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderID)); // Replace with specific domain exception
    }

    @Override
    public Set<OrderModel> findAllOrders() {
        return orderJpaRepository.findAll().stream()
                .map(persistenceOrderMapper::toModel)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        if (orderModel.getId() == null || !orderJpaRepository.existsById(orderModel.getId())) {
            throw new RuntimeException("Order with ID " + orderModel.getId() + " not found, cannot update."); // Replace with specific domain exception
        }
        OrderEntity orderEntity = persistenceOrderMapper.toEntity(orderModel);
        // Ensure items in OrderEntity have their 'order' field set
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity updatedEntity = orderJpaRepository.save(orderEntity);
        return persistenceOrderMapper.toModel(updatedEntity);
    }
}
