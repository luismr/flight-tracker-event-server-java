package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PingDTOMapperTest {

    @Test
    void shouldMapFlightDataDTOToPingDTO() {
        // Given
        Long now = Instant.now().getEpochSecond();
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
        var pingDTO = PingDTOMapper.fromFlightData(flightData);

        // Then
        assertThat(pingDTO.id()).isNotNull();
        assertThat(pingDTO.lastUpdate()).isNotNull();

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
} 