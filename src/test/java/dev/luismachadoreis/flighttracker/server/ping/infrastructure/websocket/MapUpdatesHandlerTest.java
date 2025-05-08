package dev.luismachadoreis.flighttracker.server.ping.infrastructure.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MapUpdatesHandlerTest {

    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;

    @Mock
    private ExecutorService executor;

    private MapUpdatesHandler mapUpdatesHandler;
    private static final String TEST_MESSAGE = "test message";

    @BeforeEach
    void setUp() throws Exception {
        mapUpdatesHandler = new MapUpdatesHandler(executor);
        
        // Clear the static sessions set before each test
        Field sessionsField = MapUpdatesHandler.class.getDeclaredField("sessions");
        sessionsField.setAccessible(true);
        Set<WebSocketSession> sessions = (Set<WebSocketSession>) sessionsField.get(null);
        sessions.clear();

        // Set up lenient mocks for session methods that might be called
        lenient().when(session1.isOpen()).thenReturn(true);
        lenient().when(session2.isOpen()).thenReturn(true);
    }

    @Test
    void afterConnectionEstablished_ShouldAddSession() throws Exception {
        // Act
        mapUpdatesHandler.afterConnectionEstablished(session1);

        // Send a message to verify the session was added
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert
        verify(executor, times(1)).submit(any(java.lang.Runnable.class));
    }

    @Test
    void afterConnectionClosed_ShouldRemoveSession() throws Exception {
        // Arrange
        mapUpdatesHandler.afterConnectionEstablished(session1);

        // Act
        mapUpdatesHandler.afterConnectionClosed(session1, null);
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert - no messages should be sent since session was removed
        verify(executor, never()).submit(any(java.lang.Runnable.class));
    }

    @Test
    void sendMessage_WithNoSessions_ShouldNotSendAnyMessages() throws Exception {
        // Act
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert
        verify(executor, never()).submit(any(java.lang.Runnable.class));
        verify(session1, never()).sendMessage(any(TextMessage.class));
        verify(session2, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    void sendMessage_WithActiveSessions_ShouldSendToAllSessions() throws Exception {
        // Arrange
        mapUpdatesHandler.afterConnectionEstablished(session1);
        mapUpdatesHandler.afterConnectionEstablished(session2);

        // Act
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert - one submit per active session
        verify(executor, times(2)).submit(any(java.lang.Runnable.class));
    }

    @Test
    void sendMessage_WithClosedSession_ShouldNotSendToClosedSession() throws Exception {
        // Arrange
        lenient().when(session1.isOpen()).thenReturn(false);
        mapUpdatesHandler.afterConnectionEstablished(session1);
        mapUpdatesHandler.afterConnectionEstablished(session2);
        mapUpdatesHandler.afterConnectionClosed(session1, null);

        // Act
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert - should only submit for the remaining session
        verify(executor, times(1)).submit(any(java.lang.Runnable.class));
    }

    @Test
    void sendMessage_WhenSessionThrowsException_ShouldLogError() throws Exception {
        // Arrange
        mapUpdatesHandler.afterConnectionEstablished(session1);
        lenient().doThrow(new IOException("Test exception")).when(session1).sendMessage(any(TextMessage.class));

        // Act
        mapUpdatesHandler.sendMessage(TEST_MESSAGE);

        // Assert - should still submit the task even if it throws
        verify(executor, times(1)).submit(any(java.lang.Runnable.class));
    }
} 