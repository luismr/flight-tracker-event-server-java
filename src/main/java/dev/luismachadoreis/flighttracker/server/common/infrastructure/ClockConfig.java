package dev.luismachadoreis.flighttracker.server.common.infrastructure;

import dev.luismachadoreis.flighttracker.server.common.infrastructure.config.ClockProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Configuration for Clock beans.
 */
@Configuration
@EnableConfigurationProperties(ClockProperties.class)
public class ClockConfig {

    /**
     * Creates a new Clock bean using the configured timezone.
     * @param properties The clock properties
     * @return the Clock
     */
    @Bean
    public Clock clock(ClockProperties properties) {
        return Clock.system(ZoneId.of(properties.timezone()));
    }
} 