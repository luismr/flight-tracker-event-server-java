package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;
import dev.luismachadoreis.flighttracker.server.ping.domain.event.PingCreated;
import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Publishes PingCreated events to the MapUpdatesHandler.
 */
@Slf4j
@Component
public class PingEventPublisher {

    private final MapUpdatesHandler mapUpdatesHandler;
    private final JsonUtils jsonUtils;

    /**
     * Constructor for PingEventPublisher.
     * @param mapUpdatesHandler the MapUpdatesHandler to publish to
     * @param jsonUtils the JsonUtils to use for serialization
     */
    public PingEventPublisher(MapUpdatesHandler mapUpdatesHandler, JsonUtils jsonUtils) {
        this.mapUpdatesHandler = mapUpdatesHandler;
        this.jsonUtils = jsonUtils;
    }

    /**
     * Handles PingCreated events.
     * @param event the PingCreated event
     */ 
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)   
    public void handlePingCreated(PingCreated event) {
        mapUpdatesHandler.sendMessage(jsonUtils.toJson(event));
    }
    
}