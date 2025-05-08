package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PingEventConsumerTest {

    @Mock
    private MapUpdatesHandler mapUpdatesHandler;

    private PingEventConsumer pingKafkaConsumer;

    @BeforeEach
    void setUp() {
        pingKafkaConsumer = new PingEventConsumer(mapUpdatesHandler);
    }

    @Test
    void consumePingCreated_ShouldForwardMessageToWebSocket() {
        // Arrange
        String message = "{\"id\":\"123\"}";

        // Act
        pingKafkaConsumer.consumePingCreated(message);

        // Assert
        verify(mapUpdatesHandler).sendMessage(message);
    }

    @Test
    void consumePingCreated_WhenMessageIsNull_ShouldNotForwardToWebSocket() {
        // Act
        pingKafkaConsumer.consumePingCreated(null);

        // Assert
        verify(mapUpdatesHandler, never()).sendMessage(anyString());
    }

    @Test
    void consumePingCreated_WhenMessageIsEmpty_ShouldNotForwardToWebSocket() {
        // Act
        pingKafkaConsumer.consumePingCreated("");

        // Assert
        verify(mapUpdatesHandler, never()).sendMessage(anyString());
    }
} 