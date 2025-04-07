package dev.luismachadoreis.flighttracker.server.ping.domain;    

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface PingRepository {
    <S extends Ping> S save(S ping);
    Optional<Ping> findById(UUID id);
    List<Ping> findAll();
    List<Ping> findTopNPings(Integer limit);
} 