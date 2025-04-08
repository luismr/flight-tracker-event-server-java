package dev.luismachadoreis.flighttracker.server.flightdata.infrastructure.pubsub;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.mediator.Mediator;
import dev.luismachadoreis.flighttracker.server.ping.application.CreatePingCommand;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightDataSubscriber {
    
    private final Mediator mediator;
    
    @KafkaListener(topics = "${spring.kafka.topic.flight-positions}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFlightData(FlightDataDTO data) {
        log.debug("Received flight data: {}", data);
        mediator.send(new CreatePingCommand(PingDTOMapper.fromFlightData(data)));
    }

} 