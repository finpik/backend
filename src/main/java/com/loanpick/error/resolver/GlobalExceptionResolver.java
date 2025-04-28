package com.loanpick.error.resolver;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import com.loanpick.error.dispatcher.GraphQLExceptionHandlerDispatcher;
import com.loanpick.error.handler.GraphQLExceptionHandler;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GlobalExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private final GraphQLExceptionHandlerDispatcher handlerRegistry;

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        GraphQLExceptionHandler<Throwable> handler = findHandler(ex.getClass());
        return handler.handle(ex, env);
    }

    private GraphQLExceptionHandler<Throwable> findHandler(Class<? extends Throwable> exceptionType) {
        GraphQLExceptionHandler<? extends Throwable> handler = handlerRegistry.getHandler(exceptionType);

        @SuppressWarnings("unchecked")
        GraphQLExceptionHandler<Throwable> castedHandler = (GraphQLExceptionHandler<Throwable>) handler;
        return castedHandler;
    }
}
