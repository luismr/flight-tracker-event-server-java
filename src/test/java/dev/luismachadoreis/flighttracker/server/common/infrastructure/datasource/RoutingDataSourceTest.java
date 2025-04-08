package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutingDataSourceTest {

    @Mock
    private DataSource writerDataSource;

    @Mock
    private DataSource readerDataSource;

    @Mock
    private Connection connection;

    private RoutingDataSource routingDataSource;

    @BeforeEach
    void setUp() {
        routingDataSource = new RoutingDataSource(writerDataSource, readerDataSource);
        DbContextHolder.clearDbType();
    }

    @Test
    void shouldRouteToWriter_WhenWriteContext() throws SQLException {
        // Given
        when(writerDataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);
        DbContextHolder.setDbType(DbContextHolder.WRITE);

        // When
        Object result = routingDataSource.determineCurrentLookupKey();

        // Then
        assertThat(result).isEqualTo(DbContextHolder.WRITE);
    }

    @Test
    void shouldRouteToReader_WhenReadContext() throws SQLException {
        // Given
        when(readerDataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);
        DbContextHolder.setDbType(DbContextHolder.READ);

        // When
        Object result = routingDataSource.determineCurrentLookupKey();

        // Then
        assertThat(result).isEqualTo(DbContextHolder.READ);
    }

    @Test
    void shouldFallbackToWriter_WhenReaderUnhealthy() throws SQLException {
        // Given
        when(readerDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));
        DbContextHolder.setDbType(DbContextHolder.READ);

        // When
        Object result = routingDataSource.determineCurrentLookupKey();

        // Then
        assertThat(result).isEqualTo(DbContextHolder.WRITE);
    }

    @Test
    void shouldThrowException_WhenWriterUnhealthy() throws SQLException {
        // Given
        when(writerDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));
        DbContextHolder.setDbType(DbContextHolder.WRITE);

        // When/Then
        assertThatThrownBy(() -> routingDataSource.determineCurrentLookupKey())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Writer datasource is not available");
    }

    @Test
    void shouldUseWriter_WhenNoContextSet() throws SQLException {
        // Given
        when(writerDataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);

        // When
        Object result = routingDataSource.determineCurrentLookupKey();

        // Then
        assertThat(result).isEqualTo(DbContextHolder.WRITE);
    }
} 