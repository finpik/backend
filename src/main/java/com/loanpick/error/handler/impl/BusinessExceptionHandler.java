package com.loanpick.error.handler.impl;

import org.springframework.stereotype.Component;

import com.loanpick.error.enums.GraphQLErrorType;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.error.handler.GraphQLExceptionHandler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class BusinessExceptionHandler implements GraphQLExceptionHandler<BusinessException> {

    @Override
    public Class<BusinessException> supportedExceptionType() {
        return BusinessException.class;
    }

    @Override
    public GraphQLError handle(BusinessException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env).message(ex.getMessage()).errorType(GraphQLErrorType.BUSINESS_EXCEPTION)
                .extensions(ex.getExtensions()).build();
    }
}
