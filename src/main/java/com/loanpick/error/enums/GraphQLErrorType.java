package com.loanpick.error.enums;

import graphql.ErrorClassification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GraphQLErrorType implements ErrorClassification {
    BUSINESS_EXCEPTION, INTERNAL_SERVER_ERROR, CONSTRAINT_VIOLATION_EXCEPTION, BIND_EXCEPTION
}
