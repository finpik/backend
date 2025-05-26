package finpik.global.error.handler;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

public interface GraphQLExceptionHandler<T extends Throwable> {
    Class<T> supportedExceptionType();

    GraphQLError handle(T ex, DataFetchingEnvironment env);
}
