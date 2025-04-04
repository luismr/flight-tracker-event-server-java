package dev.luismachadoreis.flighttracker.server.infrastructure.event;

import dev.luismachadoreis.flighttracker.server.application.PingService;
import dev.luismachadoreis.flighttracker.server.application.dto.PingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightDataSubscriber {
    
    private final PingService pingService;
    
    @KafkaListener(topics = "${spring.kafka.topic.flight-positions}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFlightData(PingDTO ping) {
        log.debug("Received flight data: {}", ping);
        pingService.createPing(ping);
    }

} 