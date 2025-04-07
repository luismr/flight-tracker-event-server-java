package dev.luismachadoreis.flighttracker.server.ping.application.usecase.impl;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.usecase.CreatePingUseCase;
import dev.luismachadoreis.flighttracker.server.ping.domain.Ping;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePingUseCaseImpl implements CreatePingUseCase {

    private final PingRepository pingRepository;

    @Override
    @Transactional
    public PingDTO execute(PingDTO pingDTO) {
        Ping ping = Ping.fromDTO(pingDTO);
        ping.registerPingCreated();
        return pingRepository.save(ping).toDTO();
    }
} 