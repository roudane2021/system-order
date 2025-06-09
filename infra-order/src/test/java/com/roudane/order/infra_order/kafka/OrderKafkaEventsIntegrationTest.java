package com.roudane.order.infra_order.kafka;

import com.roudane.order.domain_order.event.InventoryReservedEvent;
import com.roudane.order.domain_order.event.OrderCreatedEvent;
import com.roudane.order.domain_order.model.OrderItemModel;
import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.model.OrderStatus;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderCreateRequestDto;
import com.roudane.order.infra_order.adapter.input.rest.order.dto.OrderItemDto; // For OrderCreateRequestDto
import com.fasterxml.jackson.databind.ObjectMapper; // For converting DTO if needed by KafkaTemplate directly
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate; // For calling controller
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext; // To reset context for embedded Kafka
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.kafka.listener.DefaultErrorHandler; // For configuring error handler
import org.springframework.kafka.listener.KafkaMessageListenerContainer; // For listener container
import org.springframework.kafka.listener.MessageListener; // For listener
import org.springframework.kafka.listener.ContainerProperties; // For container properties
import org.apache.kafka.clients.consumer.ConsumerConfig; // For consumer config
import org.springframework.kafka.core.DefaultKafkaConsumerFactory; // For consumer factory
import java.util.Map; // For config map
import org.apache.kafka.common.serialization.StringDeserializer; // For string deserializer
import org.springframework.kafka.support.serializer.JsonDeserializer; // For JSON deserializer


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
               topics = {"${kafka.topics.order-created}", "${kafka.topics.inventory-reserved}", "${kafka.topics.order-shipped}"})
@DirtiesContext // Ensures Kafka broker is fresh for each test class
@ActiveProfiles("h2") // Use H2 for DB, Kafka config from application.yaml will be overridden by @EmbeddedKafka for brokers
public class OrderKafkaEventsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private IOrderPersistenceOutPort orderPersistenceOutPort; // To verify DB state

    @Value("${kafka.topics.order-created}")
    private String orderCreatedTopic;

    @Value("${kafka.topics.inventory-reserved}")
    private String inventoryReservedTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers; // Will be overridden by EmbeddedKafka but good to have for consumer setup

    @Autowired
    private ObjectMapper objectMapper; // For JSON processing

    @Test
    void shouldPublishOrderCreatedEvent_And_ConsumeInventoryReservedEventToConfirmOrder() throws Exception {
        // Part 1: Create order and verify OrderCreatedEvent is published
        // Setup a Kafka consumer manually to listen to order-created-events topic
        BlockingQueue<ConsumerRecord<String, OrderCreatedEvent>> recordsQueue = new LinkedBlockingQueue<>();

        Map<String, Object> consumerProps = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, // Use embedded Kafka brokers
            ConsumerConfig.GROUP_ID_CONFIG, "test-order-created-consumer",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TRUSTED_PACKAGES, "*",
            JsonDeserializer.USE_TYPE_INFO_HEADERS, "false"
        );
        DefaultKafkaConsumerFactory<String, OrderCreatedEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(
            consumerProps, new StringDeserializer(), new JsonDeserializer<>(OrderCreatedEvent.class, objectMapper, false)
        );
        ContainerProperties containerProps = new ContainerProperties(orderCreatedTopic);
        containerProps.setMessageListener((MessageListener<String, OrderCreatedEvent>) record -> recordsQueue.add(record));
        KafkaMessageListenerContainer<String, OrderCreatedEvent> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.setBeanName("testOrderCreatedConsumerContainer");
        container.start();


        OrderItemDto itemDto = OrderItemDto.builder().productId(101L).quantity(1).price(new BigDecimal("12.34")).build();
        OrderCreateRequestDto createRequest = OrderCreateRequestDto.builder()
                .customerId(1L)
                .items(Collections.singletonList(itemDto))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderCreateRequestDto> request = new HttpEntity<>(createRequest, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/v1/orders/create", request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Extract orderId from response if possible, or assume it's the first one created if DB is clean.
        // For simplicity, let's assume it's the only order for now.

        ConsumerRecord<String, OrderCreatedEvent> receivedEvent = recordsQueue.poll(10, TimeUnit.SECONDS);
        assertThat(receivedEvent).isNotNull();
        OrderCreatedEvent orderCreatedEvent = receivedEvent.value();
        assertThat(orderCreatedEvent).isNotNull();
        assertThat(orderCreatedEvent.getCustomerId()).isEqualTo(createRequest.getCustomerId());
        assertThat(orderCreatedEvent.getItems()).hasSize(1);
        Long orderId = orderCreatedEvent.getOrderId(); // Get the actual order ID

        container.stop(); // Stop the manual consumer

        // Part 2: Publish InventoryReservedEvent and verify order status changes to PROCESSING
        InventoryReservedEvent inventoryEvent = InventoryReservedEvent.builder()
                .orderId(orderId)
                .reservationConfirmed(true)
                .build();
        kafkaTemplate.send(inventoryReservedTopic, String.valueOf(orderId), inventoryEvent).get(5, TimeUnit.SECONDS); // Ensure it's sent

        // Allow time for @KafkaListener to process
        Thread.sleep(2000); // This is not ideal, consider Awaitility or a more robust mechanism

        OrderModel confirmedOrder = orderPersistenceOutPort.findOrderById(orderId)
                .orElseThrow(() -> new AssertionError("Order not found after inventory confirmation"));
        assertThat(confirmedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }
}
