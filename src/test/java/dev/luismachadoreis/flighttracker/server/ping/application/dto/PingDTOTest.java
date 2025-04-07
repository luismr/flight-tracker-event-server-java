package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PingDTOTest {

    @Test
    void shouldCreatePingDTO() {
        // Given
        var id = UUID.randomUUID();
        var lastUpdate = Instant.now();
        var aircraft = new PingDTO.Aircraft(
            "ABC123",
            "FL123",
            "US",
            Instant.now(),
            "7700",
            true,
            new Integer[]{1, 2}
        );
        var vector = new PingDTO.Vector(500.0, 180.0, 0.0);
        var position = new PingDTO.Position(
            10.0,
            20.0,
            30000.0,
            29000.0,
            false,
            1,
            Instant.now()
        );

        // When
        var dto = new PingDTO(id, aircraft, vector, position, lastUpdate);

        // Then
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.aircraft()).isEqualTo(aircraft);
        assertThat(dto.vector()).isEqualTo(vector);
        assertThat(dto.position()).isEqualTo(position);
        assertThat(dto.lastUpdate()).isEqualTo(lastUpdate);
    }

    @Test
    void shouldCreateNestedDTOs() {
        // Given & When
        var aircraft = new PingDTO.Aircraft(
            "ABC123",
            "FL123",
            "US",
            Instant.now(),
            "7700",
            true,
            new Integer[]{1, 2}
        );
        var vector = new PingDTO.Vector(500.0, 180.0, 0.0);
        var position = new PingDTO.Position(
            10.0,
            20.0,
            30000.0,
            29000.0,
            false,
            1,
            Instant.now()
        );

        // Then
        assertThat(aircraft.icao24()).isEqualTo("ABC123");
        assertThat(aircraft.callsign()).isEqualTo("FL123");
        assertThat(aircraft.sensors()).containsExactly(1, 2);

        assertThat(vector.velocity()).isEqualTo(500.0);
        assertThat(vector.trueTrack()).isEqualTo(180.0);
        assertThat(vector.verticalRate()).isEqualTo(0.0);

        assertThat(position.longitude()).isEqualTo(10.0);
        assertThat(position.latitude()).isEqualTo(20.0);
        assertThat(position.onGround()).isFalse();
    }

    @Test
    void shouldConvertToFlightDataDTO() {
        // Given
        var id = UUID.randomUUID();
        var lastUpdate = Instant.now();
        var aircraft = new PingDTO.Aircraft(
            "ABC123",
            "FL123",
            "US",
            Instant.now(),
            "7700",
            true,
            new Integer[]{1, 2}
        );
        var vector = new PingDTO.Vector(500.0, 180.0, 0.0);
        var position = new PingDTO.Position(
            10.0,
            20.0,
            30000.0,
            29000.0,
            false,
            1,
            Instant.now()
        );
        var dto = new PingDTO(id, aircraft, vector, position, lastUpdate);

        // When
        var flightDataDTO = dto.toFlightDataDTO();

        // Then
        assertThat(flightDataDTO.icao24()).isEqualTo(aircraft.icao24());
        assertThat(flightDataDTO.callsign()).isEqualTo(aircraft.callsign());
        assertThat(flightDataDTO.originCountry()).isEqualTo(aircraft.originCountry());
        assertThat(flightDataDTO.timePosition()).isEqualTo(position.time().getEpochSecond());
        assertThat(flightDataDTO.lastContact()).isEqualTo(aircraft.lastContact().getEpochSecond());
        assertThat(flightDataDTO.longitude()).isEqualTo(position.longitude());
        assertThat(flightDataDTO.latitude()).isEqualTo(position.latitude());
        assertThat(flightDataDTO.baroAltitude()).isEqualTo(position.baroAltitude());
        assertThat(flightDataDTO.onGround()).isEqualTo(position.onGround());
        assertThat(flightDataDTO.velocity()).isEqualTo(vector.velocity());
        assertThat(flightDataDTO.trueTrack()).isEqualTo(vector.trueTrack());
        assertThat(flightDataDTO.verticalRate()).isEqualTo(vector.verticalRate());
        assertThat(flightDataDTO.sensors()).isEqualTo(aircraft.sensors());
        assertThat(flightDataDTO.geoAltitude()).isEqualTo(position.geoAltitude());
        assertThat(flightDataDTO.squawk()).isEqualTo(aircraft.squawk());
        assertThat(flightDataDTO.spi()).isEqualTo(aircraft.spi());
        assertThat(flightDataDTO.positionSource()).isEqualTo(position.source());
    }

    @Test
    void shouldHandleNullTimestampsInFlightDataDTO() {
        // Given
        var id = UUID.randomUUID();
        var lastUpdate = Instant.now();
        var aircraft = new PingDTO.Aircraft(
            "ABC123",
            "FL123",
            "US",
            null,
            "7700",
            true,
            new Integer[]{1, 2}
        );
        var vector = new PingDTO.Vector(500.0, 180.0, 0.0);
        var position = new PingDTO.Position(
            10.0,
            20.0,
            30000.0,
            29000.0,
            false,
            1,
            null
        );
        var dto = new PingDTO(id, aircraft, vector, position, lastUpdate);

        // When
        var flightDataDTO = dto.toFlightDataDTO();

        // Then
        assertThat(flightDataDTO.lastContact()).isNull();
        assertThat(flightDataDTO.timePosition()).isNull();
    }
} 