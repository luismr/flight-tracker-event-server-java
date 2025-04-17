package dev.luismachadoreis.flighttracker.server.ping.domain;

import dev.luismachadoreis.flighttracker.server.ping.domain.event.PingCreated;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

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
        Collection<Object> events = (Collection<Object>) ReflectionTestUtils.getField(ping, "domainEvents");
        assertThat(events)
            .hasSize(1)
            .first()
            .isInstanceOf(PingCreated.class);

        var event = (PingCreated) events.iterator().next();
        assertThat(event.pingId()).isEqualTo(ping.getId());
        assertThat(event.icao24()).isEqualTo(aircraft.icao24());
        assertThat(event.callsign()).isEqualTo(aircraft.callsign());
        assertThat(event.latitude()).isEqualTo(position.latitude());
        assertThat(event.longitude()).isEqualTo(position.longitude());
        assertThat(event.trueTrack()).isEqualTo(vector.trueTrack());
        assertThat(event.geoAltitude()).isEqualTo(position.geoAltitude());
        assertThat(event.baroAltitude()).isEqualTo(position.baroAltitude());
        assertThat(event.onGround()).isEqualTo(position.onGround());
        assertThat(event.velocity()).isEqualTo(vector.velocity());
        assertThat(event.verticalRate()).isEqualTo(vector.verticalRate());
        assertThat(event.timestamp()).isEqualTo(ping.getLastUpdate());
    }
} 