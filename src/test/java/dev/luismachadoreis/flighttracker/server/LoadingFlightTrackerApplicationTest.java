package dev.luismachadoreis.flighttracker.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoadingFlightTrackerApplicationTest {

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockBean
    private ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        // If we get here, the context loaded successfully
        assertThat(kafkaTemplate).isNotNull();
        assertThat(redisConnectionFactory).isNotNull();
        assertThat(reactiveRedisConnectionFactory).isNotNull();
        assertThat(redisTemplate).isNotNull();
    }
} 