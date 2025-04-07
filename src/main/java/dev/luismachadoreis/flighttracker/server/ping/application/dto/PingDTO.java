package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public record PingDTO(
    @JsonProperty("id") UUID id,
    @JsonProperty("aircraft") Aircraft aircraft,
    @JsonProperty("vector") Vector vector,
    @JsonProperty("position") Position position,
    @JsonProperty("last_update") Instant lastUpdate
) {
    public record Aircraft(
        @JsonProperty("icao24") String icao24,
        @JsonProperty("callsign") String callsign,
        @JsonProperty("origin_country") String originCountry,
        @JsonProperty("last_contact") Instant lastContact,
        @JsonProperty("squawk") String squawk,
        @JsonProperty("spi") Boolean spi,
        @JsonProperty("sensors") Integer[] sensors
    ) {}

    public record Vector(
        @JsonProperty("velocity") Double velocity,
        @JsonProperty("true_track") Double trueTrack,
        @JsonProperty("vertical_rate") Double verticalRate
    ) {}

    public record Position(
        @JsonProperty("longitude") Double longitude,
        @JsonProperty("latitude") Double latitude,
        @JsonProperty("geo_altitude") Double geoAltitude,
        @JsonProperty("baro_altitude") Double baroAltitude,
        @JsonProperty("on_ground") Boolean onGround,
        @JsonProperty("source") Integer source,
        @JsonProperty("time") Instant time
    ) {}

    /**
     * Convert the Ping domain object to a FlightDataDTO.
     * @return The FlightDataDTO.
     */
    public FlightDataDTO toFlightDataDTO() {
        return new FlightDataDTO(
            this.aircraft.icao24(),
            this.aircraft.callsign(),
            this.aircraft.originCountry(),
            this.position.time() != null ? this.position.time().getEpochSecond() : null,
            this.aircraft.lastContact() != null ? this.aircraft.lastContact().getEpochSecond() : null,
            this.position.longitude(),
            this.position.latitude(),
            this.position.baroAltitude(),
            this.position.onGround(),
            this.vector.velocity(),
            this.vector.trueTrack(),
            this.vector.verticalRate(),
            this.aircraft.sensors(),
            this.position.geoAltitude(),
            this.aircraft.squawk(),
            this.aircraft.spi(),
            this.position.source()
        );
    }
}