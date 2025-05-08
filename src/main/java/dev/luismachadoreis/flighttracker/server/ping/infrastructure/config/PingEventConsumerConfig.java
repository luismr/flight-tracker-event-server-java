package dev.luismachadoreis.flighttracker.server.ping.infrastructure.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class PingEventConsumerConfig {

    private final Map<String, Object> properties;

    public PingEventConsumerConfig(@Qualifier("kafkaListenerContainerFactoryProperties") Map<String, Object> properties) {
        this.properties = properties;
    }
    
    @Bean(name = "pingEventConsumerFactory")
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            properties,
            new StringDeserializer(),
            new StringDeserializer()
        );
    }
    
    @Bean(name = "pingEventKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
} 