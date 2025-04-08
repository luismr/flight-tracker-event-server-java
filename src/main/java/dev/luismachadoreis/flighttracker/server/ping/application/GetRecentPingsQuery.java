package dev.luismachadoreis.flighttracker.server.ping.application;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.query.Query;
import dev.luismachadoreis.flighttracker.server.ping.application.dto.PingDTO;

import java.util.List;

/*
 * This record is a query that gets the recent pings.
 */
public record GetRecentPingsQuery(int limit) implements Query<List<PingDTO>> {} 