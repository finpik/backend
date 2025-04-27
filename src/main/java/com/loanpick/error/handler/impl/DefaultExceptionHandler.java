package com.loanpick.error.handler.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.enums.GraphQLErrorType;
import com.loanpick.error.handler.GraphQLExceptionHandler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class DefaultExceptionHandler implements GraphQLExceptionHandler<Throwable> {

    @Override
    public Class<Throwable> supportedExceptionType() {
        return Throwable.class;
    }

    @Override
    public GraphQLError handle(Throwable ex, DataFetchingEnvironment env) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return GraphqlErrorBuilder.newError(env).message("서버 내부 오류가 발생했습니다.")
                .errorType(GraphQLErrorType.INTERNAL_SERVER_ERROR)
                .extensions(Map.of("code", errorCode.getCode(), "httpStatusErrorCode", errorCode.getStatus().name(),
                        "httpStatusCode", errorCode.getStatus().value()))
                .build();
    }
}
