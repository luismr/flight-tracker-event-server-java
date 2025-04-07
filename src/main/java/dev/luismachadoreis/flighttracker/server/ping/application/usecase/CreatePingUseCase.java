package dev.luismachadoreis.flighttracker.server.ping.application.usecase;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;

public interface CreatePingUseCase {
    PingDTO execute(PingDTO pingDTO);
} 