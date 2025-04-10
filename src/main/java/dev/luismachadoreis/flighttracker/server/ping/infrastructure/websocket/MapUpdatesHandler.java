package dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles the WebSocket connection for map updates.
 */
@Slf4j
@Component
public class MapUpdatesHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();

    /**
     * Adds a new WebSocket session to the set of active sessions.
     * 
     * @param session the WebSocket session to add
     * @throws Exception if an error occurs
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    /**
     * Removes a WebSocket session from the set of active sessions.
     * 
     * @param session the WebSocket session to remove
     * @throws Exception if an error occurs
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    /**
     * Sends a message to all connected WebSocket sessions.
     * 
     * @param message the message to send
     */
    public void sendMessage(String message) {  
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Error sending message to session: {}", session.getId(), e);
            }
        });
    }

}
