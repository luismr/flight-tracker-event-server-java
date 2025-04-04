package dev.luismachadoreis.flighttracker.server.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PingCreated(
    UUID pingId,
    String icao24,
    String callsign,
    Double latitude,
    Double longitude,
    Double geoAltitude,
    Double baroAltitude,
    Boolean onGround,
    Instant timestamp
) {} 