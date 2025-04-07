package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import java.time.Instant;
import java.util.UUID;

public final class PingDTOMapper {

    /**
     * Map a FlightDataDTO to a PingDTO.
     * @param flightData The FlightDataDTO to map.
     * @return The PingDTO.
     */
    public static PingDTO fromFlightData(FlightDataDTO flightData) {
        return new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft(
                flightData.icao24(),
                flightData.callsign(),
                flightData.originCountry(),
                flightData.lastContact() != null ? Instant.ofEpochSecond(flightData.lastContact()) : null,
                flightData.squawk(),
                flightData.spi(),
                flightData.sensors()
            ),
            new PingDTO.Vector(
                flightData.velocity(),
                flightData.trueTrack(),
                flightData.verticalRate()
            ),
            new PingDTO.Position(
                flightData.longitude(),
                flightData.latitude(),
                flightData.geoAltitude(),
                flightData.baroAltitude(),
                flightData.onGround(),
                flightData.positionSource(),
                flightData.timePosition() != null ? Instant.ofEpochSecond(flightData.timePosition()) : null
            ),
            Instant.now()
        );
    }

}
