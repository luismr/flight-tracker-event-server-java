package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.util.StringUtils;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;        
import dev.luismachadoreis.flighttracker.server.ping.domain.PingCreatedEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;   


@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnExpression("${app.ping.publisher.enabled:false} == false and ${app.ping.websocket.enabled:false} == true")
public class PingEventPublisherByPassingKafka {

    private final MapUpdatesHandler mapUpdatesHandler;
    private final PingMapper pingMapper;
    private final JsonUtils jsonUtils;

    /**
     * Handles PingCreated events.
     * @param event the PingCreated event
     */ 
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)   
    public void handlePingCreated(PingCreatedEvent event) {
        PingDTO pingDTO = pingMapper.toDTO(event.ping());
        String message = jsonUtils.toJson(pingDTO);
        mapUpdatesHandler.sendMessage(message);
    }

} 