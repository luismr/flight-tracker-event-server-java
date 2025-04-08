package dev.luismachadoreis.flighttracker.server.common.application.cqs.command;

/*
 * This interface is a marker interface for command handlers that return a result of type R.
 */
public interface CommandHandler<C extends Command<R>, R> {
    
    /*
     * This method handles a command and returns a result of type R.
     */
    R handle(C command);

} 