package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import java.time.Instant;
import java.util.UUID;

public final class PingDTOMapper {

    /**
     * Map a FlightDataDTO to a PingDTO.
     * @param dto The FlightDataDTO to map.
     * @return The PingDTO.
     */
    public static PingDTO fromFlightData(FlightDataDTO dto) {
        return new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft(
                dto.icao24(),
                dto.callsign(),
                dto.originCountry(),
                dto.lastContact() != null ? Instant.ofEpochSecond(dto.lastContact()) : null,
                dto.squawk(),
                dto.spi(),
                dto.sensors()
            ),
            new PingDTO.Vector(
                dto.velocity(),
                dto.trueTrack(),
                dto.verticalRate()
            ),
            new PingDTO.Position(
                dto.longitude(),
                dto.latitude(),
                dto.geoAltitude(),
                dto.baroAltitude(),
                dto.onGround(),
                dto.positionSource(),
                dto.timePosition() != null ? Instant.ofEpochSecond(dto.timePosition()) : null
            ),
            Instant.now()
        );
    }

}
