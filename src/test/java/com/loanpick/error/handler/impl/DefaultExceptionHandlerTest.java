package com.loanpick.error.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.error.enums.ErrorCode;

import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;

class DefaultExceptionHandlerTest {

    private final DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();

    @DisplayName("미리 설정한 예외가 아닌 예외가 발생 시 서버 내부 오류로 변환한다")
    @Test
    void defaultHandlerTest() {
        // given
        Throwable throwable = new RuntimeException("unexpected error");

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("someQuery")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        // when
        GraphQLError graphQLError = defaultExceptionHandler.handle(throwable, mockEnv);

        // then
        assertThat(graphQLError.getMessage()).isEqualTo("서버 내부 오류가 발생했습니다.");

        Map<String, Object> extensions = graphQLError.getExtensions();
        assertThat(extensions).isNotNull();
        assertThat(extensions.get("code")).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        assertThat(extensions.get("httpStatusErrorCode")).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().name());
        assertThat(extensions.get("httpStatusCode")).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value());
    }
}
