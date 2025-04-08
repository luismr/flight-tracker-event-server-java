package dev.luismachadoreis.flighttracker.server.common.application.cqs.mediator;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.command.Command;
import dev.luismachadoreis.flighttracker.server.common.application.cqs.query.Query;

/*
 * This interface is a mediator that sends commands and queries to the application context.
 */ 
public interface Mediator {

    /*
     * This method sends a command to the application context and returns the result.
     */
    <R> R send(Command<R> command);

    /*
     * This method sends a query to the application context and returns the result.
     */
    <R> R send(Query<R> query);
    
} 