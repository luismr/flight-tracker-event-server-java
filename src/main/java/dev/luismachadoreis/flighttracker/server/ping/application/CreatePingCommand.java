package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.command.Command;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;

import java.util.UUID;

/*
 * This record is a command that creates a ping.
 */
public record CreatePingCommand(PingDTO pingDTO) implements Command<UUID> {} 