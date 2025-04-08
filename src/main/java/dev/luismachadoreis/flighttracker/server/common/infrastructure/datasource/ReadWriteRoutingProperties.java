package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for read-write routing.
 * 
 * @author Luis Machado Reis
 */
@ConfigurationProperties(prefix = "app.read-write-routing")
public class ReadWriteRoutingProperties {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}