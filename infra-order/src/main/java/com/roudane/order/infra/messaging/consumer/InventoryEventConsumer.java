package com.roudane.order.infra.messaging.consumer;

import com.roudane.order.domain_order.port.input.IConfirmOrderUseCase;
import com.roudane.order.domain_order.service.OrderDomain;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryEventConsumer {


    private final OrderDomain orderDomain;

    @Autowired
    public InventoryEventConsumer(OrderDomain orderDomain) {
        this.orderDomain = orderDomain;
    }

    @KafkaListener(topics = "${spring.kafka.topics.inventory-reserved:inventory-reserved-events}",
            containerFactory = "genericKafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryReservedEvent(InventoryReservedEvent event) {
        orderDomain.confirmOrder(event);
    }

    @KafkaListener(topics = "${spring.kafka.topics.inventory-depleted}",
            containerFactory = "genericKafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryDepletedEvent(InventoryDepletedEvent event) {
        orderDomain.depletedOrder(event);
    }
}
