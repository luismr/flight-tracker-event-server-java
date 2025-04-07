package dev.luismachadoreis.flighttracker.server.ping.application.usecase;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import java.util.List;

public interface GetPingsUseCase {
    List<PingDTO> execute(Integer limit);
} 