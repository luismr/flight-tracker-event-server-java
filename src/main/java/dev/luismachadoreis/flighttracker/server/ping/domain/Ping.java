package dev.luismachadoreis.flighttracker.server.ping.domain;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        registerEvent(new PingCreatedEvent(this, this.lastUpdate));
    }
    
}