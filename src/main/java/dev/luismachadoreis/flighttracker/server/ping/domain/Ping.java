package dev.luismachadoreis.flighttracker.server.ping.domain;

import dev.luismachadoreis.flighttracker.server.ping.domain.event.PingCreated;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;
import java.util.UUID;
import java.util.Arrays;

@Entity
@Getter
@NoArgsConstructor
public class Ping extends AbstractAggregateRoot<Ping> {
    
    @Id
    private UUID id;

    @Embedded
    private Aircraft aircraft;

    @Embedded
    private Vector vector;

    @Embedded
    private Position position;

    private Instant lastUpdate;
    
    public Ping(final Aircraft aircraft, final Vector vector, final Position position) {
        this.id = UUID.randomUUID();
        this.aircraft = aircraft;
        this.vector = vector;
        this.position = position;
        this.lastUpdate = Instant.now();
    }

    public PingDTO toDTO() {
        return new PingDTO(
            this.id,
            new PingDTO.Aircraft(
                this.aircraft.icao24(),
                this.aircraft.callsign(),
                this.aircraft.originCountry(),
                this.aircraft.lastContact(),
                this.aircraft.squawk(),
                this.aircraft.spi(),
                this.aircraft.sensors()
            ),
            new PingDTO.Vector(
                this.vector.velocity(),
                this.vector.trueTrack(),
                this.vector.verticalRate()
            ),
            new PingDTO.Position(
                this.position.longitude(),
                this.position.latitude(),
                this.position.geoAltitude(),
                this.position.baroAltitude(),
                this.position.onGround(),
                this.position.source(),
                this.position.time()
            ),
            this.lastUpdate
        );
    }

    public static Ping fromDTO(PingDTO dto) {
        return new Ping(
            new Aircraft(
                dto.aircraft().icao24(),
                dto.aircraft().callsign(),
                dto.aircraft().originCountry(),
                dto.aircraft().lastContact(),
                dto.aircraft().squawk(),
                dto.aircraft().spi(),
                dto.aircraft().sensors()
            ),
            new Vector(
                dto.vector().velocity(),
                dto.vector().trueTrack(),
                dto.vector().verticalRate()
            ),
            new Position(
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

    @Embeddable
    public record Aircraft(
        @Column(name = "icao24")
        String icao24,
        
        @Column(name = "callsign")
        String callsign,
        
        @Column(name = "origin_country")
        String originCountry,
        
        @Column(name = "last_contact")
        Instant lastContact,
        
        @Column(name = "squawk")
        String squawk,
        
        @Column(name = "spi")
        Boolean spi,
        
        @Column(name = "sensors")
        @Convert(converter = IntegerArrayConverter.class)
        Integer[] sensors
    ) {}

    @Embeddable
    public record Vector(
        @Column(name = "velocity")
        Double velocity,
        
        @Column(name = "true_track")
        Double trueTrack,
        
        @Column(name = "vertical_rate")
        Double verticalRate
    ) {}

    @Embeddable
    public record Position(
        @Column(name = "longitude")
        Double longitude,
        
        @Column(name = "latitude")
        Double latitude,
        
        @Column(name = "geo_altitude")
        Double geoAltitude,
        
        @Column(name = "baro_altitude")
        Double baroAltitude,
        
        @Column(name = "on_ground")
        Boolean onGround,
        
        @Column(name = "source")
        Integer source,
        
        @Column(name = "time_position")
        Instant time
    ) {}

    @Converter
    public static class IntegerArrayConverter implements AttributeConverter<Integer[], String> {
        @Override
        public String convertToDatabaseColumn(Integer[] attribute) {
            if (attribute == null) {
                return null;
            }
            if (attribute.length == 0) {
                return "";
            }
            return String.join(",", Arrays.stream(attribute)
                .map(String::valueOf)
                .toArray(String[]::new));
        }

        @Override
        public Integer[] convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            if (dbData.isEmpty()) {
                return new Integer[0];
            }
            return Arrays.stream(dbData.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
        }
    }

    /**
     * Register the PingCreated event.
     */
    public void registerPingCreated() {
        registerEvent(new PingCreated(
            this.id,
            this.aircraft.icao24(),
            this.aircraft.callsign(),
            this.position.latitude(),
            this.position.longitude(),
            this.position.geoAltitude(),
            this.position.baroAltitude(),
            this.position.onGround(),
            this.lastUpdate
        ));
    }
    
}