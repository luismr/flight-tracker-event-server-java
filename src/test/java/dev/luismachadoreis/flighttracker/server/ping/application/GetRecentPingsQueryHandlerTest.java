package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRecentPingsQueryHandlerTest {

    @Mock
    private PingRepository pingRepository;

    @Mock
    private PingMapper pingMapper;

    private GetRecentPingsQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetRecentPingsQueryHandler(pingRepository, pingMapper);
    }

    @Test
    void shouldReturnRecentPings() {
        // Given
        int limit = 2;
        Instant now = Instant.now();
        
        Ping ping1 = new Ping(
            new Ping.Aircraft("ABC123", "B737", "Boeing", now, "1234", false, new Integer[0]),
            new Ping.Vector(500.0, 180.0, 0.0),
            new Ping.Position(-0.1278, 51.5074, 1000.0, 1000.0, false, 1, now)
        );

        Ping ping2 = new Ping(
            new Ping.Aircraft("XYZ789", "A320", "Airbus", now.minusSeconds(60), "5678", false, new Integer[0]),
            new Ping.Vector(450.0, 90.0, 0.0),
            new Ping.Position(2.3522, 48.8566, 2000.0, 2000.0, false, 1, now.minusSeconds(60))
        );

        PingDTO dto1 = new PingDTO(
            ping1.getId(),
            new PingDTO.Aircraft("ABC123", "B737", "Boeing", now, "1234", false, new Integer[0]),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(-0.1278, 51.5074, 1000.0, 1000.0, false, 1, now),
            ping1.getLastUpdate()
        );

        PingDTO dto2 = new PingDTO(
            ping2.getId(),
            new PingDTO.Aircraft("XYZ789", "A320", "Airbus", now.minusSeconds(60), "5678", false, new Integer[0]),
            new PingDTO.Vector(450.0, 90.0, 0.0),
            new PingDTO.Position(2.3522, 48.8566, 2000.0, 2000.0, false, 1, now.minusSeconds(60)),
            ping2.getLastUpdate()
        );

        when(pingRepository.findTopNPings(limit)).thenReturn(List.of(ping1, ping2));
        when(pingMapper.toDTO(ping1)).thenReturn(dto1);
        when(pingMapper.toDTO(ping2)).thenReturn(dto2);

        // When
        List<PingDTO> result = handler.handle(new GetRecentPingsQuery(limit));

        // Then
        assertThat(result).hasSize(2);
        
        // Verify first ping
        PingDTO firstPing = result.get(0);
        assertThat(firstPing.aircraft().icao24()).isEqualTo("ABC123");
        assertThat(firstPing.aircraft().callsign()).isEqualTo("B737");
        assertThat(firstPing.aircraft().originCountry()).isEqualTo("Boeing");
        assertThat(firstPing.vector().velocity()).isEqualTo(500.0);
        assertThat(firstPing.vector().trueTrack()).isEqualTo(180.0);
        assertThat(firstPing.position().latitude()).isEqualTo(51.5074);
        assertThat(firstPing.position().longitude()).isEqualTo(-0.1278);

        // Verify second ping
        PingDTO secondPing = result.get(1);
        assertThat(secondPing.aircraft().icao24()).isEqualTo("XYZ789");
        assertThat(secondPing.aircraft().callsign()).isEqualTo("A320");
        assertThat(secondPing.aircraft().originCountry()).isEqualTo("Airbus");

        verify(pingMapper).toDTO(ping1);
        verify(pingMapper).toDTO(ping2);
    }

    @Test
    void shouldReturnEmptyListWhenNoRecentPings() {
        // Given
        int limit = 10;
        when(pingRepository.findTopNPings(limit)).thenReturn(List.of());

        // When
        List<PingDTO> result = handler.handle(new GetRecentPingsQuery(limit));

        // Then
        assertThat(result).isEmpty();
        verifyNoInteractions(pingMapper);
    }
} 