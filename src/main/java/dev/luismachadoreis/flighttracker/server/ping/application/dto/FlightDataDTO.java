package dev.luismachadoreis.flighttracker.server.ping.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlightDataDTO(
    @JsonProperty("icao24") String icao24,
    @JsonProperty("callsign") String callsign,
    @JsonProperty("origin_country") String originCountry,
    @JsonProperty("time_position") Long timePosition,
    @JsonProperty("last_contact") Long lastContact,
    @JsonProperty("longitude") Double longitude,
    @JsonProperty("latitude") Double latitude,
    @JsonProperty("baro_altitude") Double baroAltitude,
    @JsonProperty("on_ground") Boolean onGround,
    @JsonProperty("velocity") Double velocity,
    @JsonProperty("true_track") Double trueTrack,
    @JsonProperty("vertical_rate") Double verticalRate,
    @JsonProperty("sensors") Integer[] sensors,
    @JsonProperty("geo_altitude") Double geoAltitude,
    @JsonProperty("squawk") String squawk,
    @JsonProperty("spi") Boolean spi,
    @JsonProperty("position_source") Integer positionSource
) {} 