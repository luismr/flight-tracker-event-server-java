package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Routing datasource for read-write database operations.
 * 
 * @author Luis Machado Reis
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final DataSource writer;
    private final DataSource reader;

    private boolean readerHealthy = true;
    private long lastCheck = 0;
    private final long checkIntervalMs = 10_000;

    public RoutingDataSource(DataSource writer, DataSource reader) {
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dbType = DbContextHolder.getDbType();

        if (DbContextHolder.READ.equals(dbType)) {
            if (shouldCheckHealth() && !isReaderAvailable()) {
                readerHealthy = false;
                lastCheck = System.currentTimeMillis();

                return DbContextHolder.WRITE;
            }

            return readerHealthy ? DbContextHolder.READ : shouldReturnWriter();
        }

        return shouldReturnWriter();
    }

    private String shouldReturnWriter() {
        if (shouldCheckHealth() && !isWriterAvailable()) {
            throw new IllegalStateException("Writer datasource is not available");
        }

        return DbContextHolder.WRITE;
    }

    private boolean shouldCheckHealth() {
        return System.currentTimeMillis() - lastCheck > checkIntervalMs;
    }

    private boolean isDataSourceAvailable(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isReaderAvailable() {
        return isDataSourceAvailable(reader);
    }

    private boolean isWriterAvailable() {
        return isDataSourceAvailable(writer);
    }

}
