package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub.flightdata;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTOMapper;
import dev.luismachadoreis.flighttracker.server.ping.application.usecase.CreatePingUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightDataSubscriber {
    
    private final CreatePingUseCase createPingUseCase;
    
    @KafkaListener(topics = "${spring.kafka.topic.flight-positions}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFlightData(FlightDataDTO data) {
        log.debug("Received flight data: {}", data);
        createPingUseCase.execute(PingDTOMapper.fromFlightData(data));
    }

} 