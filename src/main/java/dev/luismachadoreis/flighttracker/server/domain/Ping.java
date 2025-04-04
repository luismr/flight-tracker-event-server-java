package dev.luismachadoreis.flighttracker.server.domain;

import dev.luismachadoreis.flighttracker.server.domain.event.PingCreated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Ping extends AbstractAggregateRoot<Ping> {
    
    @Id
    private UUID id;
    private String icao24;
    private String callsign;
    private String originCountry;
    private Long timePosition;
    private Long lastContact;
    private Double longitude;
    private Double latitude;
    private Double baroAltitude;
    private Boolean onGround;
    private Double velocity;
    private Double trueTrack;
    private Double verticalRate;
    private Integer[] sensors;
    private Double geoAltitude;
    private String squawk;
    private Boolean spi;
    private Integer positionSource;
    private Instant lastUpdate;
    
    public Ping(
        String icao24,
        String callsign,
        String originCountry,
        Long timePosition,
        Long lastContact,
        Double longitude,
        Double latitude,
        Double baroAltitude,
        Boolean onGround,
        Double velocity,
        Double trueTrack,
        Double verticalRate,
        Integer[] sensors,
        Double geoAltitude,
        String squawk,
        Boolean spi,
        Integer positionSource
    ) {
        this.id = UUID.randomUUID();
        this.icao24 = icao24;
        this.callsign = callsign;
        this.originCountry = originCountry;
        this.timePosition = timePosition;
        this.lastContact = lastContact;
        this.longitude = longitude;
        this.latitude = latitude;
        this.baroAltitude = baroAltitude;
        this.onGround = onGround;
        this.velocity = velocity;
        this.trueTrack = trueTrack;
        this.verticalRate = verticalRate;
        this.sensors = sensors;
        this.geoAltitude = geoAltitude;
        this.squawk = squawk;
        this.spi = spi;
        this.positionSource = positionSource;
        this.lastUpdate = Instant.now();
    }

    public void registerPingCreated() {
        registerEvent(new PingCreated(
            this.id,
            this.icao24,
            this.callsign,
            this.latitude,
            this.longitude,
            this.geoAltitude,
            this.baroAltitude,
            this.onGround,
            this.lastUpdate
        ));
    }
} 