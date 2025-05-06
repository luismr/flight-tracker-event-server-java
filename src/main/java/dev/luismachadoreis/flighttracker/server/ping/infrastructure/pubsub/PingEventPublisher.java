package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingCreatedEvent;
import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Publishes PingCreated events to WebSocket clients.
 */
@Slf4j
@Component
@AllArgsConstructor
public class PingEventPublisher {

    private final PingMapper pingMapper;
    private final MapUpdatesHandler mapUpdatesHandler;
    private final JsonUtils jsonUtils;

    /**
     * Handles PingCreated events.
     * @param event the PingCreated event
     */ 
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)   
    public void handlePingCreated(PingCreatedEvent event) {
        PingDTO pingDTO = pingMapper.toDTO(event.ping());
        mapUpdatesHandler.sendMessage(jsonUtils.toJson(pingDTO));
    }
    
}