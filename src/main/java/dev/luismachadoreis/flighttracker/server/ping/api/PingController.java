package dev.luismachadoreis.flighttracker.server.ping.api;

import dev.luismachadoreis.flighttracker.server.ping.application.CreatePingCommand;
import dev.luismachadoreis.flighttracker.server.ping.application.GetRecentPingsQuery;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.blueprint.cqs.SpringCommanderMediator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v2/pings")
@Tag(name = "Ping API", description = "API for managing flight position pings")
public class PingController {
    
    private final SpringCommanderMediator mediator;
    private final Integer defaultRequestLimit;

    public PingController(SpringCommanderMediator mediator, @Value("${default.request.limit:50}") Integer defaultRequestLimit) {
        this.mediator = mediator;
        this.defaultRequestLimit = defaultRequestLimit;
    }

    @Operation(
        summary = "Create a new flight position ping",
        description = "Creates a new ping with the provided flight position data",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully created ping",
                content = @Content(schema = @Schema(implementation = PingDTO.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<Void> createPing(@RequestBody PingDTO pingDTO) {
        UUID pingId = mediator.send(new CreatePingCommand(pingDTO));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(pingId)
            .toUri();

        log.info("Ping created with id: {}", pingId);
        return ResponseEntity.created(location).build();
    }

    @Operation(
        summary = "Get recent flight position pings",
        description = "Retrieves a list of recent flight position pings, limited by the specified count",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved pings",
                content = @Content(schema = @Schema(implementation = PingDTO.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<PingDTO>> getRecentPings(@RequestParam(required = false) Integer limit) {
        List<PingDTO> pings = mediator.send(
            new GetRecentPingsQuery(
                Optional.ofNullable(limit).orElse(defaultRequestLimit)
            )
        );

        log.info("Recent pings retrieved: {}", pings);
        return ResponseEntity.ok(pings);
    }

} 