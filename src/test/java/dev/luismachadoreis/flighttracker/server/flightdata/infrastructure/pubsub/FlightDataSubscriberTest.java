package dev.luismachadoreis.flighttracker.server.flightdata.infrastructure.pubsub;

import dev.luismachadoreis.blueprint.cqs.SpringCommanderMediator;
import dev.luismachadoreis.flighttracker.server.ping.application.CreatePingCommand;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.FlightDataDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightDataSubscriberTest {

    @Mock
    private SpringCommanderMediator mediator;

    @Mock
    private PingMapper pingMapper;

    private FlightDataSubscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new FlightDataSubscriber(mediator, pingMapper);
    }

    @Test
    void shouldConsumeFlightDataAndCreatePing() {
        // Given
        Long now = Instant.now().getEpochSecond();
        FlightDataDTO flightData = new FlightDataDTO(
            "ABC123",
            "FL123",
            "US",
            now,
            now,
            10.0,
            20.0,
            29000.0,
            false,
            500.0,
            180.0,
            0.0,
            new Integer[]{1, 2},
            30000.0,
            "7700",
            true,
            1
        );

        PingDTO expectedPingDTO = new PingDTO(
            UUID.randomUUID(),
            new PingDTO.Aircraft(
                flightData.icao24(),
                flightData.callsign(),
                flightData.originCountry(),
                Instant.ofEpochSecond(flightData.lastContact()),
                flightData.squawk(),
                flightData.spi(),
                flightData.sensors()
            ),
            new PingDTO.Vector(
                flightData.velocity(),
                flightData.trueTrack(),
                flightData.verticalRate()
            ),
            new PingDTO.Position(
                flightData.longitude(),
                flightData.latitude(),
                flightData.geoAltitude(),
                flightData.baroAltitude(),
                flightData.onGround(),
                flightData.positionSource(),
                Instant.ofEpochSecond(flightData.timePosition())
            ),
            Instant.now()
        );

        when(pingMapper.fromFlightData(flightData)).thenReturn(expectedPingDTO);
        when(mediator.send(any(CreatePingCommand.class))).thenReturn(expectedPingDTO.id());

        // When
        subscriber.consumeFlightData(flightData);

        // Then
        ArgumentCaptor<CreatePingCommand> commandCaptor = ArgumentCaptor.forClass(CreatePingCommand.class);
        verify(mediator).send(commandCaptor.capture());
        verify(pingMapper).fromFlightData(flightData);

        CreatePingCommand capturedCommand = commandCaptor.getValue();
        assertThat(capturedCommand.pingDTO()).isEqualTo(expectedPingDTO);
    }
} 