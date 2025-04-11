package dev.luismachadoreis.flighttracker.server.ping.domain.event;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
public record PingCreated  (
    @JsonProperty("ping_id")
    UUID pingId,
    @JsonProperty("icao24")
    String icao24,
    @JsonProperty("callsign")
    String callsign,
    @JsonProperty("latitude")
    Double latitude,
    @JsonProperty("longitude")
    Double longitude,
    @JsonProperty("true_track")
    Double trueTrack,
    @JsonProperty("geo_altitude")
    Double geoAltitude,
    @JsonProperty("baro_altitude")
    Double baroAltitude,
    @JsonProperty("on_ground")
    Boolean onGround,
    @JsonProperty("velocity")
    Double velocity,
    @JsonProperty("vertical_rate")
    Double verticalRate,
    @JsonProperty("timestamp")
    Instant timestamp
) {} 