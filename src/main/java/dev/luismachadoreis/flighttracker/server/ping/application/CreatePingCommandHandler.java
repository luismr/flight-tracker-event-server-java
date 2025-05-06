package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.blueprint.cqs.command.CommandHandler;
import dev.luismachadoreis.flighttracker.server.ping.domain.PingRepository;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/*
 * This class is a command handler that creates a ping.
 */
@Component
public class CreatePingCommandHandler implements CommandHandler<CreatePingCommand, UUID> {
    
    private final PingRepository pingRepository;
    private final PingMapper pingMapper;

    /*
     * This constructor injects the ping repository into the command handler.
     */
    public CreatePingCommandHandler(PingRepository pingRepository, PingMapper pingMapper) {
        this.pingRepository = pingRepository;
        this.pingMapper = pingMapper;
    }

    /*
     * This method handles a create ping command and returns the id of the ping.
     */
    @Override
    @Transactional
    public UUID handle(CreatePingCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        var ping = pingMapper.toDomain(command.pingDTO());
        ping.registerPingCreated();
        pingRepository.save(ping);
  
        return ping.getId();
    }
    
} 