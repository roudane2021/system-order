package com.roudane.order.infra_order.adapter.output.persistence;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional; // Recommended for test methods modifying data

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


// Ensure your main application or a test configuration has @EnableJpaRepositories
// pointing to com.roudane.order.infra_order.adapter.output.persistence.repository
// and @EntityScan for com.roudane.order.infra_order.adapter.output.persistence.entity
// If InfraOrderApplication is in a higher package and scans these, it should be fine.
@SpringBootTest // Loads the full application context
@ActiveProfiles("h2") // Activates the H2 profile and application-h2.properties
@Transactional // Roll back transactions after each test method
public class DefaultOrderPersistenceAdapterTest {

    @Autowired
    private IOrderPersistenceOutPort orderPersistenceOutPort; // Spring injects H2OrderPersistenceAdapter

    private OrderModel sampleOrderModel;

    @BeforeEach
    void setUp() {
        OrderItemModel item1 = OrderItemModel.builder()
                .productId(101L)
                .quantity(1)
                .price(new BigDecimal("10.00"))
                .build();

        sampleOrderModel = OrderModel.builder()
                .orderNumber("TEST-ORD-001")
                .orderDate(LocalDateTime.now())
                .customerId(1L)
                .status(OrderStatus.CREATED)
                .items(new ArrayList<>(Collections.singletonList(item1))) // Use mutable list
                .build();
        // item1.setOrderId() is not needed here as the adapter should handle linking
    }

    @Test
    void shouldCreateAndFindOrderById() {
        // Create Order
        OrderModel createdOrder = orderPersistenceOutPort.createOrder(sampleOrderModel);
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getOrderNumber()).isEqualTo(sampleOrderModel.getOrderNumber());
        assertThat(createdOrder.getItems()).hasSize(1);
        assertThat(createdOrder.getItems().get(0).getId()).isNotNull();
        assertThat(createdOrder.getItems().get(0).getOrderId()).isEqualTo(createdOrder.getId());


        // Find Order By Id
        Optional<OrderModel> foundOrderOpt = orderPersistenceOutPort.findOrderById(createdOrder.getId());
        assertThat(foundOrderOpt).isPresent();
        OrderModel foundOrder = foundOrderOpt.get();
        assertThat(foundOrder.getId()).isEqualTo(createdOrder.getId());
        assertThat(foundOrder.getOrderNumber()).isEqualTo(createdOrder.getOrderNumber());
        assertThat(foundOrder.getItems()).hasSize(1);
        assertThat(foundOrder.getItems().get(0).getProductId()).isEqualTo(sampleOrderModel.getItems().get(0).getProductId());
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentOrderId() {
        Optional<OrderModel> foundOrderOpt = orderPersistenceOutPort.findOrderById(999L); // Non-existent ID
        assertThat(foundOrderOpt).isNotPresent();
    }

    @Test
    void getOrderShouldThrowExceptionForNonExistentOrderId() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderPersistenceOutPort.getOrder(999L); // Non-existent ID
        });
        assertThat(exception.getMessage()).contains("Order not found with ID: 999");
    }

    @Test
    void shouldFindAllOrders() {
        orderPersistenceOutPort.createOrder(sampleOrderModel); // Create one order

        OrderModel anotherOrderModel = OrderModel.builder()
                .orderNumber("TEST-ORD-002")
                .orderDate(LocalDateTime.now().plusHours(1))
                .customerId(2L)
                .status(OrderStatus.PAID)
                .items(new ArrayList<>())
                .build();
        orderPersistenceOutPort.createOrder(anotherOrderModel);

        Set<OrderModel> allOrders = orderPersistenceOutPort.findAllOrders();
        assertThat(allOrders).hasSize(2); // Or more if tests are not cleaning up and run in sequence without @Transactional
                                          // With @Transactional at class level, each test is isolated.
    }

    @Test
    void shouldUpdateOrder() {
        OrderModel createdOrder = orderPersistenceOutPort.createOrder(sampleOrderModel);

        createdOrder.setStatus(OrderStatus.PAID);
        createdOrder.getItems().get(0).setQuantity(5);

        OrderModel updatedOrder = orderPersistenceOutPort.updateOrder(createdOrder);

        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getId()).isEqualTo(createdOrder.getId());
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(updatedOrder.getItems()).hasSize(1);
        assertThat(updatedOrder.getItems().get(0).getQuantity()).isEqualTo(5);

        // Verify by fetching again
        OrderModel fetchedOrder = orderPersistenceOutPort.findOrderById(createdOrder.getId()).orElse(null);
        assertThat(fetchedOrder).isNotNull();
        assertThat(fetchedOrder.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(fetchedOrder.getItems().get(0).getQuantity()).isEqualTo(5);
    }
}
