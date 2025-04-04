package dev.luismachadoreis.flighttracker.server.api;

import dev.luismachadoreis.flighttracker.server.application.PingService;
import dev.luismachadoreis.flighttracker.server.application.dto.PingDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pings")
public class PingController {
    
    private final PingService pingService;
    private final int defaultRequestLimit;
    
    public PingController(
        PingService pingService,
        @Value("${default.request.limit:50}") int defaultRequestLimit
    ) {
        this.pingService = pingService;
        this.defaultRequestLimit = defaultRequestLimit;
    }

    @GetMapping
    public ResponseEntity<List<PingDTO>> getPings(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(pingService.getPings(
            Optional.ofNullable(limit).orElse(defaultRequestLimit)
        ));
    }

} 