package dev.luismachadoreis.flighttracker.server.common.infrastructure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource.DbContextHolder;
import dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource.ReadWriteRoutingProperties;
import dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource.RoutingDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.Map;
import java.util.HashMap;
import javax.sql.DataSource;

/**
 * Configuration for datasources.
 * 
 * @author Luis Machado Reis
 */
@Configuration
@EnableConfigurationProperties(ReadWriteRoutingProperties.class)
public class DatasourceConfig {

    @Bean(name = "writerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.writer")
    public DataSource writerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "readerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.reader")
    public DataSource readerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dataSource(
            @Qualifier("writerDataSource") DataSource writer,
            @Qualifier("readerDataSource") DataSource reader,
            ReadWriteRoutingProperties props
    ) {
        if (!props.isEnabled()) {
            return writer; // routing is OFF
        }

        RoutingDataSource routingDataSource = new RoutingDataSource(writer, reader);

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DbContextHolder.WRITE, writer);
        dataSources.put(DbContextHolder.READ, reader);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writer);

        return routingDataSource;
    }

}
