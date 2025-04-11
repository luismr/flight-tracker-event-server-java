package dev.luismachadoreis.flighttracker.server.ping.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PingCreatedTest {

    @Test
    void shouldCreatePingCreatedEvent() {
        // Given
        var id = UUID.randomUUID();
        var icao24 = "ABC123";
        var callsign = "FL123";
        var latitude = 10.0;
        var longitude = 20.0;
        var geoAltitude = 30000.0;
        var baroAltitude = 29000.0;
        var onGround = false;
        var velocity = 100.0;
        var verticalRate = 1000.0;
        var timestamp = Instant.now();
        var trueTrack = 10.0;
        // When
        var event = new PingCreated(
            id,
            icao24,
            callsign,   
            latitude,
            longitude,  
            trueTrack,
            geoAltitude,
            baroAltitude,
            onGround,
            velocity,
            verticalRate,
            timestamp
        );

        // Then
        assertThat(event.pingId()).isEqualTo(id);
        assertThat(event.icao24()).isEqualTo(icao24);
        assertThat(event.callsign()).isEqualTo(callsign);
        assertThat(event.latitude()).isEqualTo(latitude);
        assertThat(event.longitude()).isEqualTo(longitude);
        assertThat(event.geoAltitude()).isEqualTo(geoAltitude);
        assertThat(event.baroAltitude()).isEqualTo(baroAltitude);
        assertThat(event.onGround()).isEqualTo(onGround);
        assertThat(event.timestamp()).isEqualTo(timestamp);
    }
} 