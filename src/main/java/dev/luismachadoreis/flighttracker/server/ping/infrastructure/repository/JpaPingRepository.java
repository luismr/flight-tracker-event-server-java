package dev.luismachadoreis.flighttracker.server.ping.infrastructure.repository;

import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface JpaPingRepository extends JpaRepository<Ping, UUID>, PingRepository {
    
    @Override
    default <S extends Ping> S save(S ping) {
        return saveAndFlush(ping);
    }
    
    @Query(value = "SELECT p FROM Ping p ORDER BY p.position.time DESC LIMIT :limit")
    List<Ping> findTopNPingsOrderByTimePositionDesc(Integer limit);

    @Override
    default List<Ping> findTopNPings(Integer limit) {
        return findTopNPingsOrderByTimePositionDesc(limit);
    }
} 