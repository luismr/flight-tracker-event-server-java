package dev.luismachadoreis.flighttracker.server.ping.infrastructure.pubsub;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingCreatedEvent;
import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import dev.luismachadoreis.flighttracker.server.common.utils.JsonUtils;

@ExtendWith(MockitoExtension.class)
class PingEventPublisherByPassingKafkaTest {

    @Mock
    private MapUpdatesHandler mapUpdatesHandler;

    @Mock
    private PingMapper pingMapper;

    @Mock
    private JsonUtils jsonUtils;

    private PingEventPublisherByPassingKafka publisher;

    @BeforeEach
    void setUp() {
        publisher = new PingEventPublisherByPassingKafka(mapUpdatesHandler, pingMapper, jsonUtils);
    }

    @Test
    void handlePingCreated_ShouldSendMessageToWebSocket() {
        // Arrange
        Instant now = Instant.now();
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", now, "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, now);
        var ping = new Ping(aircraft, vector, position);
        var event = new PingCreatedEvent(ping, now);

        var pingDTO = new PingDTO(
            ping.getId(),
            new PingDTO.Aircraft(
                aircraft.icao24(),
                aircraft.callsign(),
                aircraft.originCountry(),
                aircraft.lastContact(),
                aircraft.squawk(),
                aircraft.spi(),
                aircraft.sensors()
            ),
            new PingDTO.Vector(
                vector.velocity(),
                vector.trueTrack(),
                vector.verticalRate()
            ),
            new PingDTO.Position(
                position.longitude(),
                position.latitude(),
                position.geoAltitude(),
                position.baroAltitude(),
                position.onGround(),
                position.source(),
                position.time()
            ),
            ping.getLastUpdate()
        );

        String expectedJson = "{\"id\":\"" + ping.getId() + "\",\"aircraft\":{\"icao24\":\"ABC123\",\"callsign\":\"FL123\",\"origin_country\":\"US\",\"last_contact\":\"" + now + "\",\"squawk\":\"7700\",\"spi\":true,\"sensors\":[1,2]},\"vector\":{\"velocity\":500.0,\"true_track\":180.0,\"vertical_rate\":0.0},\"position\":{\"longitude\":10.0,\"latitude\":20.0,\"geo_altitude\":30000.0,\"baro_altitude\":29000.0,\"on_ground\":false,\"source\":1,\"time\":\"" + now + "\"},\"last_update\":\"" + now + "\"}";

        when(pingMapper.toDTO(ping)).thenReturn(pingDTO);
        when(jsonUtils.toJson(pingDTO)).thenReturn(expectedJson);

        // Act
        publisher.handlePingCreated(event);

        // Assert
        verify(pingMapper).toDTO(ping);
        verify(jsonUtils).toJson(pingDTO);
        verify(mapUpdatesHandler).sendMessage(expectedJson);
    }
} 