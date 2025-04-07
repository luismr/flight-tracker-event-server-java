package dev.luismachadoreis.flighttracker.server.ping.application.usecase.impl;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePingUseCaseImplTest {

    @Mock
    private PingRepository pingRepository;

    @InjectMocks
    private CreatePingUseCaseImpl createPingUseCase;

    @Test
    void shouldCreatePing() {
        // Given
        var dto = new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2}),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now()),
            Instant.now()
        );

        var ping = Ping.fromDTO(dto);
        when(pingRepository.save(any(Ping.class))).thenReturn(ping);

        // When
        var result = createPingUseCase.execute(dto);

        // Then
        verify(pingRepository, times(1)).save(any(Ping.class));
        assertThat(result).isNotNull();
        assertThat(result.aircraft().icao24()).isEqualTo(dto.aircraft().icao24());
        assertThat(result.vector().velocity()).isEqualTo(dto.vector().velocity());
        assertThat(result.position().latitude()).isEqualTo(dto.position().latitude());
    }
} 