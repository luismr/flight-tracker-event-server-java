package dev.luismachadoreis.flighttracker.server.infrastructure.event;

import dev.luismachadoreis.flighttracker.server.domain.event.PingCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PingEventPublisher {

    @EventListener   
    public void handlePingCreated(PingCreated event) {
        log.info("Ping created: icao24={}, callsign={}, at {}", 
            event.icao24(), 
            event.callsign(), 
            event.timestamp());
    }


    
}