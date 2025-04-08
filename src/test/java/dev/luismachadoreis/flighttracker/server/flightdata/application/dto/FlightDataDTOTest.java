package dev.luismachadoreis.flighttracker.server.flightdata.application.dto;

import org.junit.jupiter.api.Test;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;

import static org.assertj.core.api.Assertions.assertThat;

class FlightDataDTOTest {

    @Test
    void shouldCreateFlightDataDTO() {
        // Given
        String icao24 = "ABC123";
        String callsign = "FL123";
        String originCountry = "US";
        Long timePosition = 1234567890L;
        Long lastContact = 1234567891L;
        Double longitude = 10.0;
        Double latitude = 20.0;
        Double baroAltitude = 29000.0;
        Boolean onGround = false;
        Double velocity = 500.0;
        Double trueTrack = 180.0;
        Double verticalRate = 0.0;
        Integer[] sensors = {1, 2};
        Double geoAltitude = 30000.0;
        String squawk = "7700";
        Boolean spi = true;
        Integer positionSource = 1;

        // When
        var dto = new FlightDataDTO(
            icao24, callsign, originCountry, timePosition, lastContact,
            longitude, latitude, baroAltitude, onGround, velocity,
            trueTrack, verticalRate, sensors, geoAltitude,
            squawk, spi, positionSource
        );

        // Then
        assertThat(dto.icao24()).isEqualTo(icao24);
        assertThat(dto.callsign()).isEqualTo(callsign);
        assertThat(dto.originCountry()).isEqualTo(originCountry);
        assertThat(dto.timePosition()).isEqualTo(timePosition);
        assertThat(dto.lastContact()).isEqualTo(lastContact);
        assertThat(dto.longitude()).isEqualTo(longitude);
        assertThat(dto.latitude()).isEqualTo(latitude);
        assertThat(dto.baroAltitude()).isEqualTo(baroAltitude);
        assertThat(dto.onGround()).isEqualTo(onGround);
        assertThat(dto.velocity()).isEqualTo(velocity);
        assertThat(dto.trueTrack()).isEqualTo(trueTrack);
        assertThat(dto.verticalRate()).isEqualTo(verticalRate);
        assertThat(dto.sensors()).containsExactly(1, 2);
        assertThat(dto.geoAltitude()).isEqualTo(geoAltitude);
        assertThat(dto.squawk()).isEqualTo(squawk);
        assertThat(dto.spi()).isEqualTo(spi);
        assertThat(dto.positionSource()).isEqualTo(positionSource);
    }
} 