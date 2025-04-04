package dev.luismachadoreis.flighttracker.server.infrastructure;

import dev.luismachadoreis.flighttracker.server.domain.Ping;
import dev.luismachadoreis.flighttracker.server.domain.PingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
public interface JpaPingRepository extends JpaRepository<Ping, UUID>, PingRepository {
    
    @Override
    default <S extends Ping> S save(S ping) {
        return saveAndFlush(ping);
    }
    
    @Override
    default Optional<Ping> findById(UUID id) {
        return Optional.ofNullable(findById(id).orElse(null));
    }
    
    @Query(value = "SELECT p FROM Ping p ORDER BY p.timePosition DESC LIMIT :limit")
    List<Ping> findTopNPingsOrderByTimePositionDesc(Integer limit);

    @Override
    default List<Ping> findTopNPings(Integer limit) {
        return findTopNPingsOrderByTimePositionDesc(limit);
    }
} 