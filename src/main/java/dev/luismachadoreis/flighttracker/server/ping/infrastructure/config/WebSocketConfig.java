package dev.luismachadoreis.flighttracker.server.ping.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket.MapUpdatesHandler;
import lombok.AllArgsConstructor;

/**
 * Configuration for the WebSocket server.
 */
@Configuration
@EnableWebSocket
@AllArgsConstructor
@ConditionalOnProperty(prefix = "app.ping.websocket", name = "enabled", havingValue = "true")
public class WebSocketConfig implements WebSocketConfigurer {
    private final MapUpdatesHandler mapUpdatesHandler;

    /**
     * Registers the WebSocket handlers.
     * 
     * @param registry the WebSocket handler registry
     */
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(mapUpdatesHandler, "/map-updates").setAllowedOrigins("*");
    }

} 