package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Publishes PingCreated events to Kafka.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.ping.publisher", name = "enabled", havingValue = "true")
public class PingEventPublisher {

    private final PingMapper pingMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonUtils jsonUtils;
    private final String pingCreatedTopic;

    public PingEventPublisher(
        final PingMapper pingMapper, 
        final KafkaTemplate<String, String> kafkaTemplate, 
        final JsonUtils jsonUtils, 
        @Value("${spring.kafka.topic.ping-created}") 
        final String pingCreatedTopic
    ) {
        this.pingMapper = pingMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.jsonUtils = jsonUtils;
        this.pingCreatedTopic = pingCreatedTopic;
    }

    /**
     * Handles PingCreated events.
     * @param event the PingCreated event
     */ 
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)   
    public void handlePingCreated(PingCreatedEvent event) {
        PingDTO pingDTO = pingMapper.toDTO(event.ping());
        String message = jsonUtils.toJson(pingDTO);
        kafkaTemplate.send(pingCreatedTopic, message);
        log.debug("Published ping created event to Kafka topic {}: {}", pingCreatedTopic, message);
    }
}