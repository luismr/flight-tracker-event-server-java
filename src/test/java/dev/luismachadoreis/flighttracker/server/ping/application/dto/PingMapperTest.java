package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PingMapperTest {

    private PingMapper mapper;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("UTC"));
        mapper = new PingMapper(fixedClock);
    }

    @Test
    void shouldMapFlightDataToPingDTO() {
        // Given
        Long now = Instant.now(fixedClock).getEpochSecond();
        var flightData = new FlightDataDTO(
            "ABC123",
            "FL123",
            "US",
            now,
            now,
            10.0,
            20.0,
            29000.0,
            false,
            500.0,
            180.0,
            0.0,
            new Integer[]{1, 2},
            30000.0,
            "7700",
            true,
            1
        );

        // When
        var pingDTO = mapper.fromFlightData(flightData);

        // Then
        assertThat(pingDTO.id()).isNotNull();
        assertThat(pingDTO.lastUpdate()).isEqualTo(Instant.now(fixedClock));

        // Aircraft data
        assertThat(pingDTO.aircraft().icao24()).isEqualTo(flightData.icao24());
        assertThat(pingDTO.aircraft().callsign()).isEqualTo(flightData.callsign());
        assertThat(pingDTO.aircraft().originCountry()).isEqualTo(flightData.originCountry());
        assertThat(pingDTO.aircraft().lastContact()).isEqualTo(Instant.ofEpochSecond(flightData.lastContact()));
        assertThat(pingDTO.aircraft().squawk()).isEqualTo(flightData.squawk());
        assertThat(pingDTO.aircraft().spi()).isEqualTo(flightData.spi());
        assertThat(pingDTO.aircraft().sensors()).isEqualTo(flightData.sensors());

        // Vector data
        assertThat(pingDTO.vector().velocity()).isEqualTo(flightData.velocity());
        assertThat(pingDTO.vector().trueTrack()).isEqualTo(flightData.trueTrack());
        assertThat(pingDTO.vector().verticalRate()).isEqualTo(flightData.verticalRate());

        // Position data
        assertThat(pingDTO.position().longitude()).isEqualTo(flightData.longitude());
        assertThat(pingDTO.position().latitude()).isEqualTo(flightData.latitude());
        assertThat(pingDTO.position().geoAltitude()).isEqualTo(flightData.geoAltitude());
        assertThat(pingDTO.position().baroAltitude()).isEqualTo(flightData.baroAltitude());
        assertThat(pingDTO.position().onGround()).isEqualTo(flightData.onGround());
        assertThat(pingDTO.position().source()).isEqualTo(flightData.positionSource());
        assertThat(pingDTO.position().time()).isEqualTo(Instant.ofEpochSecond(flightData.timePosition()));
    }

    @Test
    void shouldMapPingToDTO() {
        // Given
        var now = Instant.now(fixedClock);
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", now, "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, now);
        var ping = new Ping(aircraft, vector, position);

        // When
        var dto = mapper.toDTO(ping);

        // Then
        assertThat(dto.id()).isEqualTo(ping.getId());
        assertThat(dto.aircraft().icao24()).isEqualTo(aircraft.icao24());
        assertThat(dto.aircraft().callsign()).isEqualTo(aircraft.callsign());
        assertThat(dto.vector().velocity()).isEqualTo(vector.velocity());
        assertThat(dto.position().latitude()).isEqualTo(position.latitude());
        assertThat(dto.lastUpdate()).isEqualTo(ping.getLastUpdate());
    }

    @Test
    void shouldMapDTOToDomain() {
        // Given
        var now = Instant.now(fixedClock);
        var dto = new PingDTO(
            null,
            new PingDTO.Aircraft("ABC123", "FL123", "US", now, "7700", true, new Integer[]{1, 2}),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, now),
            null
        );

        // When
        var ping = mapper.toDomain(dto);

        // Then
        assertThat(ping.getId()).isNotNull();
        assertThat(ping.getAircraft().icao24()).isEqualTo(dto.aircraft().icao24());
        assertThat(ping.getVector().velocity()).isEqualTo(dto.vector().velocity());
        assertThat(ping.getPosition().latitude()).isEqualTo(dto.position().latitude());
        assertThat(ping.getLastUpdate()).isNotNull();
    }
} 