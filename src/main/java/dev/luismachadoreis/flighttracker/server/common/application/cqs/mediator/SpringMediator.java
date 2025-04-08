package dev.luismachadoreis.flighttracker.server.common.application.cqs.mediator;

import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import dev.luismachadoreis.flighttracker.server.common.application.cqs.command.Command;
import dev.luismachadoreis.flighttracker.server.common.application.cqs.command.CommandHandler;
import dev.luismachadoreis.flighttracker.server.common.application.cqs.query.Query;
import dev.luismachadoreis.flighttracker.server.common.application.cqs.query.QueryHandler;

import java.util.Arrays;
import java.util.Objects;

/*
 * This class is a mediator that sends commands and queries to the application context.
 */
@Component
public class SpringMediator implements Mediator {
    private final ApplicationContext applicationContext;

    /*
     * This constructor injects the application context into the mediator.
     */
    public SpringMediator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*
     * This method sends a command to the application context and returns the result.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(Command<R> command) {
        Objects.requireNonNull(command, "Command cannot be null");

        Class<?> commandClass = command.getClass();
        
        return Arrays.stream(applicationContext.getBeanNamesForType(CommandHandler.class))
            .map(applicationContext::getBean)
                .filter(handler -> {
                    Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);
                    return generics != null && generics[0].equals(commandClass);
                })
                    .findFirst()
                        .map(
                            handler -> ((CommandHandler<Command<R>, R>) handler).handle(command)
                        )
                    .orElseThrow(
                        () -> new IllegalStateException("No handler found for command: %s".formatted(commandClass.getName()))
                    );
    }

    /*
     * This method sends a query to the application context and returns the result.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(Query<R> query) {
        Objects.requireNonNull(query, "Query cannot be null");
        
        Class<?> queryClass = query.getClass();

        return Arrays.stream(applicationContext.getBeanNamesForType(QueryHandler.class))
            .map(applicationContext::getBean)
                .filter(handler -> {
                    Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handler.getClass(), QueryHandler.class);
                    return generics != null && generics[0].equals(queryClass);
                })
                    .findFirst()
                            .map(
                                handler -> (
                                    (QueryHandler<Query<R>, R>) handler).handle(query)
                            )
                        .orElseThrow(
                            () -> new IllegalStateException("No handler found for query: %s".formatted(queryClass.getName()))
                        );
    }
} 