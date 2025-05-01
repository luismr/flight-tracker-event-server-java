package dev.luismachadoreis.flighttracker.server.ping.domain;

import java.time.Instant;
public record PingCreatedEvent  (
    Ping ping,
    Instant timestamp
) {} 