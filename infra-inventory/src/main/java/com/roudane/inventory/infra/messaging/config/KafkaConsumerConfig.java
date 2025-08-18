package com.roudane.inventory.infra.messaging.config;

import com.roudane.transverse.event.OrderCancelledEvent;
import com.roudane.transverse.event.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    // Generic consumer factory for JSON events
    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> eventTypeClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // Configure JsonDeserializer
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(eventTypeClass);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(true);

        ErrorHandlingDeserializer<T> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);


        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer);
    }

    @Bean("orderCreatedEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> orderCreatedEventContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(OrderCreatedEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> orderCancelledEventContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(OrderCancelledEvent.class));
        return factory;
    }
}
