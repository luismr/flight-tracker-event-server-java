package dev.luismachadoreis.flighttracker.server.common.infrastructure;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.HashMap;

@Configuration
public class KafkaConfig {

    private final String bootstrapServers;
    private final String groupId;
    private final String trustedPackages;

    public KafkaConfig(
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
        @Value("${spring.kafka.consumer.group-id}") String groupId,
        @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}") String trustedPackages
    ) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.trustedPackages = trustedPackages;
    }

    @Bean(name = "kafkaListenerContainerFactoryProperties")
    public Map<String, Object> kafkaListenerContainerFactoryProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);

        return props;
    }
}
