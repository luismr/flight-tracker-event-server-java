package dev.luismachadoreis.flighttracker.server.ping.application.usecase.impl;

import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPingsUseCaseImplTest {

    @Mock
    private PingRepository pingRepository;

    @InjectMocks
    private GetPingsUseCaseImpl getPingsUseCase;

    @Test
    void shouldGetPingsWithLimit() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());
        var ping = new Ping(aircraft, vector, position);
        
        when(pingRepository.findTopNPings(2)).thenReturn(List.of(ping, ping));

        // When
        var result = getPingsUseCase.execute(2);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).aircraft().icao24()).isEqualTo(aircraft.icao24());
        assertThat(result.get(0).vector().velocity()).isEqualTo(vector.velocity());
        assertThat(result.get(0).position().latitude()).isEqualTo(position.latitude());
    }

    @Test
    void shouldReturnEmptyListWhenNoPings() {
        // Given
        when(pingRepository.findTopNPings(10)).thenReturn(List.of());

        // When
        var result = getPingsUseCase.execute(10);

        // Then
        assertThat(result).isEmpty();
    }
} 