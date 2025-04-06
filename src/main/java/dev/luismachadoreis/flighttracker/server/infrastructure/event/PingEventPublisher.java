package dev.luismachadoreis.flighttracker.server.infrastructure.event;

import dev.luismachadoreis.flighttracker.server.domain.event.PingCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class PingEventPublisher {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)   
    public void handlePingCreated(PingCreated event) {
        log.info("Ping created: icao24={}, callsign={}, at {}", 
            event.icao24(), 
            event.callsign(), 
            event.timestamp());
    }
    
}