package com.loanpick.error.handler.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.enums.GraphQLErrorType;
import com.loanpick.error.handler.GraphQLExceptionHandler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;

@Component
public class ConstraintViolationExceptionHandler implements GraphQLExceptionHandler<ConstraintViolationException> {
    @Override
    public Class<ConstraintViolationException> supportedExceptionType() {
        return ConstraintViolationException.class;
    }

    @Override
    public GraphQLError handle(ConstraintViolationException ex, DataFetchingEnvironment env) {
        List<Map<String, String>> fieldErrors = getFieldErrors(ex);

        return GraphqlErrorBuilder.newError(env).message(ex.getMessage()).errorType(GraphQLErrorType.CONSTRAINT_VIOLATION_EXCEPTION)
                .extensions(getExtensions(fieldErrors)).build();
    }

    private List<Map<String, String>> getFieldErrors(ConstraintViolationException cve) {
        return cve.getConstraintViolations().stream().map(
                violation -> Map.of("field", violation.getPropertyPath().toString(), "message", violation.getMessage()))
                .toList();
    }

    private Map<String, Object> getExtensions(List<Map<String, String>> fieldErrors) {
        ErrorCode errorCode = ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION;

        return Map.of("code", errorCode.getCode(), "httpStatusErrorCode", errorCode.getStatus().name(),
                "httpStatusCode", errorCode.getStatus().value(), "fieldErrors", fieldErrors);
    }
}
