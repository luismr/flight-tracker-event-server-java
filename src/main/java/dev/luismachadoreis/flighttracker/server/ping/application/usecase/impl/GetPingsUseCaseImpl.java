package dev.luismachadoreis.flighttracker.server.ping.application.usecase.impl;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.usecase.GetPingsUseCase;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPingsUseCaseImpl implements GetPingsUseCase {

    private final PingRepository pingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PingDTO> execute(Integer limit) {
        return pingRepository.findTopNPings(limit).stream()
            .map(ping -> ping.toDTO())
            .toList();
    }
} 