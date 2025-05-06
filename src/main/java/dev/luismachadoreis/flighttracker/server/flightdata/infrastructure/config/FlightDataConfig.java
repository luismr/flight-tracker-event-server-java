package dev.luismachadoreis.flighttracker.server.flightdata.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app.flight-data")
@Getter
@Setter
public class FlightDataConfig {
    private Subscriber subscriber = new Subscriber();

    @Getter
    @Setter
    public static class Subscriber {
        private boolean enabled = true;
    }
} 