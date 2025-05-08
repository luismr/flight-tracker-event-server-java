package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;

import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumes ping events from Kafka and forwards them to WebSocket clients.
 */
@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(prefix = "app.ping.websocket", name = "enabled", havingValue = "true")
public class PingEventConsumer {

    private final MapUpdatesHandler mapUpdatesHandler;

    /**
     * Consumes ping created events from Kafka and forwards them to WebSocket clients.
     * 
     * @param message the ping created event
     */
    @KafkaListener(topics = "${spring.kafka.topic.ping-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePingCreated(String message) {
        log.debug("Received ping created event from Kafka: {}", message);
        if (StringUtils.hasText(message)) {
            mapUpdatesHandler.sendMessage(message);
        } else {
            log.debug("Skipping empty or null message");
        }
    }

} 