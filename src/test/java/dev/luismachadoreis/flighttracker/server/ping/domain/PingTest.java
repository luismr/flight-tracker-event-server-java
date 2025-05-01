package dev.luismachadoreis.flighttracker.server.ping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PingTest {

    @Test
    void shouldCreatePingFromConstructor() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());

        // When
        var ping = new Ping(aircraft, vector, position);

        // Then
        assertThat(ping.getId()).isNotNull();
        assertThat(ping.getAircraft()).isEqualTo(aircraft);
        assertThat(ping.getVector()).isEqualTo(vector);
        assertThat(ping.getPosition()).isEqualTo(position);
        assertThat(ping.getLastUpdate()).isNotNull();
    }

    @Test
    void shouldRegisterPingCreatedEvent() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());
        var ping = new Ping(aircraft, vector, position);

        // When
        ping.registerPingCreated();

        // Then
        @SuppressWarnings("unchecked")
        var events = Optional.ofNullable(
                (Collection<Object>) ReflectionTestUtils.getField(ping, "domainEvents")
            ).orElseThrow(() -> new IllegalStateException("Domain events collection is null"));

        assertThat(events)
            .hasSize(1)
            .first()
            .isInstanceOf(PingCreatedEvent.class);

        var event = (PingCreatedEvent) events.stream().findFirst().orElseThrow(() -> new IllegalStateException("No event found"));
        assertThat(event.ping()).isEqualTo(ping);
        assertThat(event.timestamp()).isEqualTo(ping.getLastUpdate());
    }
} 