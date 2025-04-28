package com.loanpick.error.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;

import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;

class BusinessExceptionHandlerTest {
    private final BusinessExceptionHandler businessExceptionHandler = new BusinessExceptionHandler();

    @DisplayName("BusinessException 발생 시 GraphQLError로 변환한다")
    @Test
    void handleBusinessException() {
        // given
        Map<String, Object> extensions = Map.of("code", "BUSINESS_ERROR", "reason", "DuplicatedUser");
        BusinessException businessException = new BusinessException(ErrorCode.TEST_EXCEPTION);

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        // when
        GraphQLError graphQLError = businessExceptionHandler.handle(businessException, mockEnv);
        Map<String, Object> actualExtensions = graphQLError.getExtensions();

        // then
        Assertions.assertAll(() -> assertThat(graphQLError.getMessage()).isEqualTo("test용 에러"),
                () -> assertThat(actualExtensions).isNotNull(),
                () -> assertThat(actualExtensions.get("httpStatusErrorCode")).isEqualTo("BAD_REQUEST"),
                () -> assertThat(actualExtensions.get("httpStatusCode")).isEqualTo(400));
    }
}
