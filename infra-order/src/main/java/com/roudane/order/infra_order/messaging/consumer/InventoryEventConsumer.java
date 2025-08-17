package com.roudane.order.infra_order.messaging.consumer;

import com.roudane.transverse.event.InventoryReservedEvent;
import com.roudane.order.domain_order.port.input.IConfirmOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryEventConsumer.class);
    private final IConfirmOrderUseCase confirmOrderUseCase;

    @Autowired
    public InventoryEventConsumer(IConfirmOrderUseCase confirmOrderUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    @KafkaListener(topics = "${kafka.topics.inventory-reserved:inventory-reserved-events}",containerFactory = "InventoryReservedEventContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryReservedEvent(InventoryReservedEvent event) {
        LOGGER.info("Received InventoryReservedEvent for orderId: {}, confirmation: {}", event.getOrderId(), event.isReservationConfirmed());
        if (event.isReservationConfirmed()) {
            try {
                confirmOrderUseCase.confirmOrder(event.getOrderId());
                LOGGER.info("Order {} processed for inventory confirmation.", event.getOrderId());
            } catch (Exception e) {
                LOGGER.error("Error processing inventory confirmation for orderId {}: {}", event.getOrderId(), e.getMessage(), e);

            }
        } else {
            LOGGER.warn("Inventory reservation failed or was not confirmed for orderId: {}. Further action might be needed (e.g., cancel order).", event.getOrderId());
        }
    }
}
