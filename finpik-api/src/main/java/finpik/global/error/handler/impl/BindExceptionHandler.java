package finpik.global.error.handler.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import finpik.error.enums.ErrorCode;
import finpik.global.error.enums.GraphQLErrorType;
import finpik.global.error.handler.GraphQLExceptionHandler;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class BindExceptionHandler implements GraphQLExceptionHandler<BindException> {
    @Override
    public Class<BindException> supportedExceptionType() {
        return BindException.class;
    }

    @Override
    public GraphQLError handle(BindException ex, DataFetchingEnvironment env) {
        List<Map<String, String>> fieldErrors = getFieldErrors(ex);

        return GraphqlErrorBuilder.newError(env).message(ex.getMessage()).errorType(GraphQLErrorType.BIND_EXCEPTION)
                .extensions(getExtensions(fieldErrors)).build();
    }

    private List<Map<String, String>> getFieldErrors(BindException be) {
        return be.getBindingResult().getFieldErrors().stream().map(error -> Map.of("field", error.getField(), "message",
                error.getDefaultMessage() != null ? error.getDefaultMessage() : "")).toList();
    }

    private Map<String, Object> getExtensions(List<Map<String, String>> fieldErrors) {
        ErrorCode errorCode = ErrorCode.BIND_EXCEPTION;

        return Map.of("code", errorCode.getCode(), "httpStatusErrorCode", errorCode.getStatus().name(),
                "httpStatusCode", errorCode.getStatus().getValue(), "fieldErrors", fieldErrors);
    }
}
