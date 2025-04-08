package dev.luismachadoreis.flighttracker.server.flightdata.infrastructure.kafka;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class FlightDataConsumerConfig {

    private final Map<String, Object> properties;

    public FlightDataConsumerConfig(@Qualifier("kafkaListenerContainerFactoryProperties") Map<String, Object> properties) {
        this.properties = properties;
    }
    
    @Bean
    public ConsumerFactory<String, FlightDataDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            properties,
            new StringDeserializer(),
            new JsonDeserializer<>(FlightDataDTO.class)
        );
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FlightDataDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FlightDataDTO> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    
} 