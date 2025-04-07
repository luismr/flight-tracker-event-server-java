package dev.luismachadoreis.flighttracker.server.ping.api;

import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.ping.application.usecase.CreatePingUseCase;
import dev.luismachadoreis.flighttracker.server.ping.application.usecase.GetPingsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Ping API", description = "API for managing flight position pings")
@RestController
@RequestMapping("/api/pings")
public class PingController {
    
    private final CreatePingUseCase createPingUseCase;
    private final GetPingsUseCase getPingsUseCase;
    private final int defaultRequestLimit;

    public PingController(CreatePingUseCase createPingUseCase, GetPingsUseCase getPingsUseCase, @Value("${default.request.limit:50}") int defaultRequestLimit) {
        this.createPingUseCase = createPingUseCase;
        this.getPingsUseCase = getPingsUseCase;
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
    public ResponseEntity<PingDTO> createPing(@RequestBody PingDTO pingDTO) {
        return ResponseEntity.ok(createPingUseCase.execute(pingDTO));
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
    public ResponseEntity<List<PingDTO>> getPings(
        @Parameter(
            description = "Maximum number of pings to retrieve",
            example = "50"
        )
        @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(getPingsUseCase.execute(
            Optional.ofNullable(limit).orElse(defaultRequestLimit)
        ));
    }
} 