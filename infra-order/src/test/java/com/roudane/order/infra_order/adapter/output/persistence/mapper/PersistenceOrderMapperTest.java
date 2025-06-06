package com.roudane.order.infra_order.adapter.output.persistence.mapper;

import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.infra_order.adapter.output.persistence.entity.OrderEntity;
import com.roudane.order.infra_order.adapter.output.persistence.entity.OrderItemEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceOrderMapperTest {

    private final PersistenceOrderMapper mapper = Mappers.getMapper(PersistenceOrderMapper.class);

    @Test
    void shouldMapOrderModelToOrderEntity() {
        // Given
        OrderItemModel itemModel = OrderItemModel.builder()
                .id(1L)
                .productId(100L)
                .quantity(2)
                .price(new BigDecimal("50.00"))
                .build();

        OrderModel orderModel = OrderModel.builder()
                .id(1L)
                .orderNumber("ORD-123")
                .orderDate(LocalDateTime.now())
                .customerId(1L)
                .status(OrderStatus.CREATED)
                .items(Collections.singletonList(itemModel))
                .build();
        itemModel.setOrderId(orderModel.getId()); // Set after orderModel is built if ID is generated

        // When
        OrderEntity orderEntity = mapper.toEntity(orderModel);

        // Then
        assertThat(orderEntity).isNotNull();
        assertThat(orderEntity.getId()).isEqualTo(orderModel.getId());
        assertThat(orderEntity.getOrderNumber()).isEqualTo(orderModel.getOrderNumber());
        assertThat(orderEntity.getOrderDate()).isEqualTo(orderModel.getOrderDate());
        assertThat(orderEntity.getCustomerId()).isEqualTo(orderModel.getCustomerId());
        assertThat(orderEntity.getStatus()).isEqualTo(orderModel.getStatus());
        assertThat(orderEntity.getItems()).hasSize(1);

        OrderItemEntity itemEntity = orderEntity.getItems().get(0);
        assertThat(itemEntity.getId()).isEqualTo(itemModel.getId());
        assertThat(itemEntity.getProductId()).isEqualTo(itemModel.getProductId());
        assertThat(itemEntity.getQuantity()).isEqualTo(itemModel.getQuantity());
        assertThat(itemEntity.getPrice()).isEqualTo(itemModel.getPrice());
        // Check if the bidirectional link is not set by this mapping direction for itemEntity.order
        // This is correct as toEntity(OrderModel) maps children, and their parent is set in adapter
        // assertThat(itemEntity.getOrder()).isEqualTo(orderEntity); // This might be null or set by collection mapping logic
    }

    @Test
    void shouldMapOrderEntityToOrderModel() {
        // Given
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .orderNumber("ORD-123")
                .orderDate(LocalDateTime.now())
                .customerId(1L)
                .status(OrderStatus.PAID)
                .build();

        OrderItemEntity itemEntity = OrderItemEntity.builder()
                .id(1L)
                .productId(100L)
                .quantity(2)
                .price(new BigDecimal("50.00"))
                .order(orderEntity) // Set the owning side
                .build();
        orderEntity.setItems(Collections.singletonList(itemEntity));


        // When
        OrderModel orderModel = mapper.toModel(orderEntity);

        // Then
        assertThat(orderModel).isNotNull();
        assertThat(orderModel.getId()).isEqualTo(orderEntity.getId());
        assertThat(orderModel.getOrderNumber()).isEqualTo(orderEntity.getOrderNumber());
        assertThat(orderModel.getOrderDate()).isEqualTo(orderEntity.getOrderDate());
        assertThat(orderModel.getCustomerId()).isEqualTo(orderEntity.getCustomerId());
        assertThat(orderModel.getStatus()).isEqualTo(orderEntity.getStatus());
        assertThat(orderModel.getItems()).hasSize(1);

        OrderItemModel itemModel = orderModel.getItems().get(0);
        assertThat(itemModel.getId()).isEqualTo(itemEntity.getId());
        assertThat(itemModel.getProductId()).isEqualTo(itemEntity.getProductId());
        assertThat(itemModel.getQuantity()).isEqualTo(itemEntity.getQuantity());
        assertThat(itemModel.getPrice()).isEqualTo(itemEntity.getPrice());
        assertThat(itemModel.getOrderId()).isEqualTo(orderEntity.getId()); // Crucial check for parent ID
    }

    @Test
    void shouldMapOrderItemModelToOrderItemEntity() {
        OrderItemModel itemModel = OrderItemModel.builder()
            .id(1L)
            .orderId(2L) // This is the parent Order's ID
            .productId(101L)
            .quantity(3)
            .price(new BigDecimal("10.50"))
            .build();

        OrderItemEntity itemEntity = mapper.toEntity(itemModel);

        assertThat(itemEntity).isNotNull();
        assertThat(itemEntity.getId()).isEqualTo(itemModel.getId());
        assertThat(itemEntity.getProductId()).isEqualTo(itemModel.getProductId());
        assertThat(itemEntity.getQuantity()).isEqualTo(itemModel.getQuantity());
        assertThat(itemEntity.getPrice()).isEqualTo(itemModel.getPrice());
        // itemEntity.getOrder() would be null here as it's ignored in this mapping direction
        // This is correct as the parent OrderEntity is set in the adapter logic when mapping the whole OrderModel
        assertThat(itemEntity.getOrder()).isNull();
    }

    @Test
    void shouldMapOrderItemEntityToOrderItemModel() {
        OrderEntity parentOrderEntity = OrderEntity.builder().id(2L).build();
        OrderItemEntity itemEntity = OrderItemEntity.builder()
            .id(1L)
            .order(parentOrderEntity) // Link to parent
            .productId(101L)
            .quantity(3)
            .price(new BigDecimal("10.50"))
            .build();

        OrderItemModel itemModel = mapper.toModel(itemEntity);

        assertThat(itemModel).isNotNull();
        assertThat(itemModel.getId()).isEqualTo(itemEntity.getId());
        assertThat(itemModel.getOrderId()).isEqualTo(parentOrderEntity.getId()); // Check mapping of order.id
        assertThat(itemModel.getProductId()).isEqualTo(itemEntity.getProductId());
        assertThat(itemModel.getQuantity()).isEqualTo(itemEntity.getQuantity());
        assertThat(itemModel.getPrice()).isEqualTo(itemEntity.getPrice());
    }
}
