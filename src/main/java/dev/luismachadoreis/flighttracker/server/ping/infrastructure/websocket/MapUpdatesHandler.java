package dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.lang.NonNull;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutorService;

/**
 * Handles WebSocket connections and message sending for map updates.
 */
@Slf4j
@Component
public class MapUpdatesHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();

    private final ExecutorService executor;

    public MapUpdatesHandler(ExecutorService webSocketExecutorService) {
        this.executor = webSocketExecutorService;
    }

    /**
     * Adds a new WebSocket session to the set of active sessions.
     * 
     * @param session the WebSocket session to add
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    /**
     * Removes a WebSocket session from the set of active sessions.
     * 
     * @param session the WebSocket session to remove
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    /**
     * Sends a message to all connected WebSocket sessions.
     * 
     * @param message the message to send
     */
    public void sendMessage(String message) {
        if (sessions.isEmpty()) {
            return;
        }

        log.debug("Sending message to {} sessions", sessions.size());

        sessions.forEach(session -> 
            executor.submit(() -> sendMessageToSession(message, session))
        );
    }

    /**
     * Sends a message to a specific WebSocket session.
     * 
     * @param message the message to send
     * @param session the WebSocket session to send the message to
     */
    private void sendMessageToSession(String message, WebSocketSession session) {
        if (session == null || !session.isOpen()) {
            return;
        }
        
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("Error sending message to session: {}", session.getId(), e);
        }
    }

}
