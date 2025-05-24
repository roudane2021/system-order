package com.roudane.order.infra_order.adapter.output.persistence;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class IOrderPersistenceAdapter implements IOrderPersistenceOutPort {
    /**
     * @param orderModel
     * @return
     */
    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<OrderModel> findAllOrders() {
        return null;
    }

    /**
     * @param orderID
     * @return
     */
    @Override
    public OrderModel getOrder(Long orderID) {
        return null;
    }

    /**
     * @param orderModel
     * @return
     */
    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        return null;
    }

    /**
     * @param orderID
     * @return
     */
    @Override
    public Optional<OrderModel> findOrderById(Long orderID) {
        return Optional.of(OrderModel.builder().id(orderID).orderDate(LocalDateTime.now()).status(OrderStatus.CREATED).build());
    }
}
