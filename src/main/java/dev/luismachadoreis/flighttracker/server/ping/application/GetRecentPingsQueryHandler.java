package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.query.QueryHandler;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
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

    /*
     * This constructor injects the ping repository into the query handler.
     */
    public GetRecentPingsQueryHandler(PingRepository pingRepository) {
        this.pingRepository = pingRepository;
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
            .map(ping -> ping.toDTO())
            .toList();
    }
} 