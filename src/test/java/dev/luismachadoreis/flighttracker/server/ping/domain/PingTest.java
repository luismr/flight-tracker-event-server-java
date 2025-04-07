package dev.luismachadoreis.flighttracker.server.ping.domain;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.domain.event.PingCreated;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class PingTest {

    @Test
    void shouldCreatePingFromConstructor() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());

        // When
        var ping = new Ping(aircraft, vector, position);

        // Then
        assertThat(ping.getId()).isNotNull();
        assertThat(ping.getAircraft()).isEqualTo(aircraft);
        assertThat(ping.getVector()).isEqualTo(vector);
        assertThat(ping.getPosition()).isEqualTo(position);
        assertThat(ping.getLastUpdate()).isNotNull();
    }

    @Test
    void shouldConvertToDTO() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());
        var ping = new Ping(aircraft, vector, position);

        // When
        var dto = ping.toDTO();

        // Then
        assertThat(dto.id()).isEqualTo(ping.getId());
        assertThat(dto.aircraft().icao24()).isEqualTo(aircraft.icao24());
        assertThat(dto.aircraft().callsign()).isEqualTo(aircraft.callsign());
        assertThat(dto.vector().velocity()).isEqualTo(vector.velocity());
        assertThat(dto.position().latitude()).isEqualTo(position.latitude());
        assertThat(dto.lastUpdate()).isEqualTo(ping.getLastUpdate());
    }

    @Test
    void shouldCreateFromDTO() {
        // Given
        var dto = new PingDTO(
            null,
            new PingDTO.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2}),
            new PingDTO.Vector(500.0, 180.0, 0.0),
            new PingDTO.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now()),
            null
        );

        // When
        var ping = Ping.fromDTO(dto);

        // Then
        assertThat(ping.getId()).isNotNull();
        assertThat(ping.getAircraft().icao24()).isEqualTo(dto.aircraft().icao24());
        assertThat(ping.getVector().velocity()).isEqualTo(dto.vector().velocity());
        assertThat(ping.getPosition().latitude()).isEqualTo(dto.position().latitude());
        assertThat(ping.getLastUpdate()).isNotNull();
    }

    @Test
    void shouldRegisterPingCreatedEvent() {
        // Given
        var aircraft = new Ping.Aircraft("ABC123", "FL123", "US", Instant.now(), "7700", true, new Integer[]{1, 2});
        var vector = new Ping.Vector(500.0, 180.0, 0.0);
        var position = new Ping.Position(10.0, 20.0, 30000.0, 29000.0, false, 1, Instant.now());
        var ping = new Ping(aircraft, vector, position);

        // When
        ping.registerPingCreated();

        // Then
        @SuppressWarnings("unchecked")
        Collection<Object> events = (Collection<Object>) ReflectionTestUtils.getField(ping, "domainEvents");
        assertThat(events)
            .hasSize(1)
            .first()
            .isInstanceOf(PingCreated.class);

        var event = (PingCreated) events.iterator().next();
        assertThat(event.pingId()).isEqualTo(ping.getId());
        assertThat(event.icao24()).isEqualTo(aircraft.icao24());
        assertThat(event.callsign()).isEqualTo(aircraft.callsign());
    }

    @Nested
    class IntegerArrayConverterTest {
        private final Ping.IntegerArrayConverter converter = new Ping.IntegerArrayConverter();

        @Test
        void shouldConvertArrayToString() {
            // Given
            Integer[] sensors = {1, 2, 3, 4, 5};

            // When
            String result = converter.convertToDatabaseColumn(sensors);

            // Then
            assertThat(result).isEqualTo("1,2,3,4,5");
        }

        @Test
        void shouldConvertStringToArray() {
            // Given
            String dbData = "1,2,3,4,5";

            // When
            Integer[] result = converter.convertToEntityAttribute(dbData);

            // Then
            assertThat(result).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        void shouldHandleNullInputForDatabaseColumn() {
            // When
            String result = converter.convertToDatabaseColumn(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        void shouldHandleNullInputForEntityAttribute() {
            // When
            Integer[] result = converter.convertToEntityAttribute(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        void shouldHandleEmptyArray() {
            // Given
            Integer[] sensors = {};

            // When
            String result = converter.convertToDatabaseColumn(sensors);
            Integer[] converted = converter.convertToEntityAttribute(result);

            // Then
            assertThat(result).isEmpty();
            assertThat(converted).isEmpty();
        }
    }
} 