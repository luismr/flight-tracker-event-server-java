package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import org.springframework.stereotype.Component;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Component
public class PingMapper {
    
    private final Clock clock;

    public PingMapper(Clock clock) {
        this.clock = clock;
    }
    
    /**
     * Map a Ping to a PingDTO.
     * @param ping The Ping to map.
     * @return The PingDTO.
     */
    public PingDTO toDTO(Ping ping) {
        return new PingDTO(
            ping.getId(),
            new PingDTO.Aircraft(
                ping.getAircraft().icao24(),
                ping.getAircraft().callsign(),
                ping.getAircraft().originCountry(),
                ping.getAircraft().lastContact(),
                ping.getAircraft().squawk(),
                ping.getAircraft().spi(),
                ping.getAircraft().sensors()
            ),
            new PingDTO.Vector(
                ping.getVector().velocity(),
                ping.getVector().trueTrack(),
                ping.getVector().verticalRate()
            ),
            new PingDTO.Position(
                ping.getPosition().longitude(),
                ping.getPosition().latitude(),
                ping.getPosition().geoAltitude(),
                ping.getPosition().baroAltitude(),
                ping.getPosition().onGround(),
                ping.getPosition().source(),
                ping.getPosition().time()
            ),
            ping.getLastUpdate()
        );
    }

    /**
     * Map a PingDTO to a Ping.
     * @param dto The PingDTO to map.
     * @return The Ping.
     */
    public Ping toDomain(PingDTO dto) {
        return new Ping(
            new Ping.Aircraft(
                dto.aircraft().icao24(),
                dto.aircraft().callsign(),
                dto.aircraft().originCountry(),
                dto.aircraft().lastContact(),
                dto.aircraft().squawk(),
                dto.aircraft().spi(),
                dto.aircraft().sensors()
            ),
            new Ping.Vector(
                dto.vector().velocity(),
                dto.vector().trueTrack(),
                dto.vector().verticalRate()
            ),
            new Ping.Position(
                dto.position().longitude(),
                dto.position().latitude(),
                dto.position().geoAltitude(),
                dto.position().baroAltitude(),
                dto.position().onGround(),
                dto.position().source(),
                dto.position().time()
            )
        );
    }

    /**
     * Map a FlightDataDTO to a PingDTO.
     * @param flightData The FlightDataDTO to map.
     * @return The PingDTO.
     */
    public PingDTO fromFlightData(FlightDataDTO flightData) {
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
            Instant.now(clock)
        );
    }
} 