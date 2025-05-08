package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingCreatedEvent;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PingEventPublisherTest {

    @Mock
    private PingMapper pingMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private Ping ping;

    private PingEventPublisher pingEventPublisher;
    private static final String PING_CREATED_TOPIC = "ping-created-topic";

    @BeforeEach
    void setUp() {
        pingEventPublisher = new PingEventPublisher(pingMapper, kafkaTemplate, jsonUtils, PING_CREATED_TOPIC);
    }

    @Test
    void handlePingCreated_ShouldPublishEventToKafka() {
        // Arrange
        Instant now = Instant.now();
        PingCreatedEvent event = new PingCreatedEvent(ping, now);
        
        PingDTO pingDTO = new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft("ABC123", "FL123", "US", now, "7700", true, new Integer[]{1, 2}),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, now),
            now
        );
        
        String jsonMessage = "{\"id\":\"123\"}";

        when(pingMapper.toDTO(ping)).thenReturn(pingDTO);
        when(jsonUtils.toJson(pingDTO)).thenReturn(jsonMessage);

        // Act
        pingEventPublisher.handlePingCreated(event);

        // Assert
        verify(pingMapper).toDTO(ping);
        verify(jsonUtils).toJson(pingDTO);
        verify(kafkaTemplate).send(PING_CREATED_TOPIC, jsonMessage);
    }

    @Test
    void handlePingCreated_WhenJsonConversionFails_ShouldNotPublish() {
        // Arrange
        Instant now = Instant.now();
        PingCreatedEvent event = new PingCreatedEvent(ping, now);
        
        PingDTO pingDTO = new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft("ABC123", "FL123", "US", now, "7700", true, new Integer[]{1, 2}),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, now),
            now
        );

        when(pingMapper.toDTO(ping)).thenReturn(pingDTO);
        when(jsonUtils.toJson(pingDTO)).thenThrow(new RuntimeException("JSON conversion failed"));

        // Act & Assert
        try {
            pingEventPublisher.handlePingCreated(event);
        } catch (RuntimeException e) {
            // Expected exception
        }

        verify(pingMapper).toDTO(ping);
        verify(jsonUtils).toJson(pingDTO);
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
} 