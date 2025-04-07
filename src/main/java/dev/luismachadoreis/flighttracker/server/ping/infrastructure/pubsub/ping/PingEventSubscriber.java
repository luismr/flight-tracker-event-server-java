package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub.ping;

import dev.luismachadoreis.flighttracker.server.ping.domain.event.PingCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;  
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class PingEventSubscriber {
    
    @KafkaListener(topics = "${spring.kafka.topic.ping-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePingEvent(PingCreated event) {
        log.info("Received ping event from Kafka: {}", event);
    }

}