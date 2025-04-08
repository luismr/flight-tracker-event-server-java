package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePingCommandHandlerTest {

    @Mock
    private PingRepository pingRepository;

    private CreatePingCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreatePingCommandHandler(pingRepository);
    }

    @Test
    void shouldCreatePingAndReturnId() {
        // Given
        Instant now = Instant.now();
        PingDTO pingDTO = new PingDTO(
            null,
            new PingDTO.Aircraft("ABC123", "B737", "Boeing", now, "1234", false, new Integer[0]),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(-0.1278, 51.5074, 1000.0, 1000.0, false, 1, now),
            now
        );
        CreatePingCommand command = new CreatePingCommand(pingDTO);

        when(pingRepository.save(any(Ping.class))).thenAnswer(invocation -> {
            Ping savedPing = invocation.getArgument(0);
            return new Ping(
                savedPing.getAircraft(),
                savedPing.getVector(),
                savedPing.getPosition()
            );
        });

        // When
        UUID actualId = handler.handle(command);

        // Then
        assertThat(actualId).isNotNull();
        verify(pingRepository).save(any(Ping.class));
    }

    @Test
    void shouldHandleNullLastUpdate() {
        // Given
        Instant now = Instant.now();
        PingDTO pingDTO = new PingDTO(
            null,
            new PingDTO.Aircraft("ABC123", "B737", "Boeing", now, "1234", false, new Integer[0]),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(-0.1278, 51.5074, 1000.0, 1000.0, false, 1, now),
            null
        );
        CreatePingCommand command = new CreatePingCommand(pingDTO);

        when(pingRepository.save(any(Ping.class))).thenAnswer(invocation -> {
            Ping savedPing = invocation.getArgument(0);
            return new Ping(
                savedPing.getAircraft(),
                savedPing.getVector(),
                savedPing.getPosition()
            );
        });

        // When
        UUID actualId = handler.handle(command);

        // Then
        assertThat(actualId).isNotNull();
        verify(pingRepository).save(argThat(ping -> 
            ping.getLastUpdate() != null && 
            ping.getAircraft().icao24().equals("ABC123")
        ));
    }
} 