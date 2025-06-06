package dev.luismachadoreis.flighttracker.server.ping.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuration for the WebSocket thread pool using virtual threads.
 */
@Configuration
@ConditionalOnBean(WebSocketConfig.class)
public class WebSocketThreadPoolConfig {

    /**
     * Creates a new executor service using virtual threads.
     * 
     * @return the new executor service
     */
    @Bean
    public ExecutorService webSocketExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    
} 