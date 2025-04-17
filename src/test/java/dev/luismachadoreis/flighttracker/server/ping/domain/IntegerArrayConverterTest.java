package dev.luismachadoreis.flighttracker.server.ping.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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