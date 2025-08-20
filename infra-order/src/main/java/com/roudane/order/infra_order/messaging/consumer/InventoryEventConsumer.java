package com.roudane.order.infra_order.messaging.consumer;

import com.roudane.order.domain_order.port.input.IConfirmOrderUseCase;
import com.roudane.transverse.event.InventoryReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryEventConsumer {


    private final IConfirmOrderUseCase confirmOrderUseCase;

    @Autowired
    public InventoryEventConsumer(IConfirmOrderUseCase confirmOrderUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    @KafkaListener(topics = "${kafka.topics.inventory-reserved:inventory-reserved-events}",containerFactory = "InventoryReservedEventContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryReservedEvent(InventoryReservedEvent event) {

        if (event.isReservationConfirmed()) {
            confirmOrderUseCase.confirmOrder(event.getOrderId());
        } else {
            log.warn("Inventory reservation failed or was not confirmed for orderId: {}. Further action might be needed (e.g., cancel order).", event.getOrderId());
        }
    }
}
