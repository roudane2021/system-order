package com.roudane.order.infra.messaging.consumer;


import com.roudane.order.domain_order.service.OrderApplicationService;
import com.roudane.order.infra.service.OrderApplicationFacade;
import com.roudane.transverse.event.InventoryDepletedEvent;
import com.roudane.transverse.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {


    private final OrderApplicationFacade orderApplication;


    @KafkaListener(topics = "${spring.kafka.topics.inventory-reserved:inventory-reserved-events}",
            containerFactory = "genericKafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryReservedEvent(InventoryReservedEvent event) {
        orderApplication.confirmOrder(event);
    }

    @KafkaListener(topics = "${spring.kafka.topics.inventory-depleted}",
            containerFactory = "genericKafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryDepletedEvent(InventoryDepletedEvent event) {
        orderApplication.depletedOrder(event);
    }
}
