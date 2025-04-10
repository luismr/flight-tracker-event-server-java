package dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configures the WebSocket endpoints and handlers.
 */
@Configuration
@EnableWebSocket
public class MapUpdatesConfig implements WebSocketConfigurer {

    private final MapUpdatesHandler mapUpdatesHandler;

    /**
     * Constructor for MapUpdatesConfig.
     * 
     * @param mapUpdatesHandler the handler for map updates
     */
    public MapUpdatesConfig(MapUpdatesHandler mapUpdatesHandler) {
        this.mapUpdatesHandler = mapUpdatesHandler;
    }

    /**
     * Registers the WebSocket handlers.
     * 
     * @param registry the WebSocket handler registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mapUpdatesHandler, "/map-updates").setAllowedOrigins("*");
    }

}
