package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.blueprint.cqs.query.QueryHandler;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
/*
 * This class is a query handler that gets the recent pings.
 */
@Component
public class GetRecentPingsQueryHandler implements QueryHandler<GetRecentPingsQuery, List<PingDTO>> {
    private final PingRepository pingRepository;
    private final PingMapper pingMapper;

    /*
     * This constructor injects the ping repository into the query handler.
     */
    public GetRecentPingsQueryHandler(PingRepository pingRepository, PingMapper pingMapper) {
        this.pingRepository = pingRepository;
        this.pingMapper = pingMapper;
    }

    /*
     * This method handles a get recent pings query and returns the recent pings.
     */ 
    @Override
    @Transactional(readOnly = true)
    public List<PingDTO> handle(GetRecentPingsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        return pingRepository.findTopNPings(query.limit())
            .stream()
            .map(pingMapper::toDTO)
            .toList();
    }
} 