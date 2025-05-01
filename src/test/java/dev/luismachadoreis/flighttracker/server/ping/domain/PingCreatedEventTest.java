package dev.luismachadoreis.flighttracker.server.ping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class PingCreatedEventTest {

    @Test
    void shouldCreatePingCreatedEvent() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());
        var ping = new Ping(aircraft, vector, position);
        var timestamp = Instant.now();

        // When
        var event = new PingCreatedEvent(ping, timestamp);

        // Then
        assertThat(event.ping()).isEqualTo(ping);
        assertThat(event.timestamp()).isEqualTo(timestamp);
    }
} 