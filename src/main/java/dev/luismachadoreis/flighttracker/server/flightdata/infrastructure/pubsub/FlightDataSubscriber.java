package dev.luismachadoreis.flighttracker.server.flightdata.infrastructure.pubsub;

import dev.luismachadoreis.blueprint.cqs.SpringCommanderMediator;
import dev.luismachadoreis.flighttracker.server.ping.application.CreatePingCommand;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.flight-data.subscriber", name = "enabled", havingValue = "true")
public class FlightDataSubscriber {
    
    private final SpringCommanderMediator mediator;
    private final PingMapper pingMapper;
    
    @KafkaListener(topics = "${spring.kafka.topic.flight-positions}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFlightData(FlightDataDTO data) {
        log.debug("Received flight data: {}", data);
        mediator.send(new CreatePingCommand(pingMapper.fromFlightData(data)));
    }

} 