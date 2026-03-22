package com.roudane.order.infra.messaging.config;

import com.roudane.transverse.exception.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> genericKafkaListenerContainerFactory(DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(genericJsonConsumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    /**
     * Crée un DeadLetterPublishingRecoverer qui redirige les messages en échec
     * vers un topic suffixé par ".DLT", en conservant la même partition.
     *
     * @param template KafkaTemplate utilisé pour publier dans le DLT
     * @return recoverer configuré pour la publication en Dead Letter Topic
     */
    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, Object> template) {
        return new DeadLetterPublishingRecoverer(template,
                (record, exception) -> new TopicPartition(record.topic() + ".DLT", record.partition()));
    }


    /**
     * Configure un DefaultErrorHandler avec retry (3 tentatives, 1s d'intervalle)
     * et envoi en DLT via le recoverer lorsque les retries sont épuisés.
     *
     * Exceptions non-retryables : envoi direct en DLT.
     * Exceptions retryables : 3 tentatives avant DLT.
     *
     * @param recoverer composant chargé de publier les messages en échec dans le DLT
     * @return handler configuré pour la gestion des erreurs Kafka
     */
    @Bean
    public DefaultErrorHandler errorHandler(final DeadLetterPublishingRecoverer recoverer) {

        // Retry 3 fois avec 1 seconde entre chaque
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

        // Exceptions qui NE DOIVENT PAS être retentées → DLQ direct
        handler.addNotRetryableExceptions(
                NotFoundException.class
        );

        // RuntimeException → retry 3 fois → DLQ
        handler.addRetryableExceptions(RuntimeException.class);

        return handler;
    }

    /**
     * Crée un ConsumerFactory générique configuré pour consommer des messages JSON.
     * Utilise un JsonDeserializer avec packages de confiance ouverts et désactive
     * le type mapper pour les clés.
     *
     * @return ConsumerFactory configuré pour la désérialisation JSON
     */
    private ConsumerFactory<String, Object> genericJsonConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }



}
