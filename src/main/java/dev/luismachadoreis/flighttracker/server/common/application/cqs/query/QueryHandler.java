package dev.luismachadoreis.flighttracker.server.common.application.cqs.query;

/*
 * This interface is a marker interface for query handlers that return a result of type R.
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /*
     * This method handles a query and returns a result of type R.
     */
    R handle(Q query);
    
} 