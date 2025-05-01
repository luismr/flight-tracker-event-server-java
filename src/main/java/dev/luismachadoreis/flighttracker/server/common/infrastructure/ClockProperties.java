package dev.luismachadoreis.flighttracker.server.common.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for clock settings.
 */
@ConfigurationProperties(prefix = "app.clock")
public record ClockProperties(
    /**
     * The timezone to use for the clock.
     * Defaults to "UTC" if not specified.
     */
    String timezone
) {
    public ClockProperties {
        timezone = Objects.requireNonNullElse(timezone, "UTC");
    }
} 