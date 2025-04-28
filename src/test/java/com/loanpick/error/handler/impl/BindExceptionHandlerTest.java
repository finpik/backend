package com.loanpick.error.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.loanpick.error.enums.ErrorCode;

import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;

class BindExceptionHandlerTest {
    private final BindExceptionHandler bindExceptionHandler = new BindExceptionHandler();

    @DisplayName("BindException 발생 시 GraphQLError로 변환한다")
    @Test
    void handleBindException() {
        // given
        BindException bindException = new BindException(new Object(), "target");
        bindException.addError(new FieldError("target", "username", "이름을 넣어주세요."));
        bindException.addError(new FieldError("target", "provider", "회원가입에 사용한 벤더사를 넣어주세요."));

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        // when
        GraphQLError graphQLError = bindExceptionHandler.handle(bindException, mockEnv);
        Map<String, Object> extensions = graphQLError.getExtensions();

        // then
        assertAll(() -> assertThat(graphQLError.getMessage()).isEqualTo(bindException.getMessage()),
                () -> assertThat(extensions).isNotNull(),
                () -> assertThat(extensions.get("code")).isEqualTo(ErrorCode.BIND_EXCEPTION.getCode()),
                () -> assertThat(extensions.get("httpStatusErrorCode"))
                        .isEqualTo(ErrorCode.BIND_EXCEPTION.getStatus().name()),
                () -> assertThat(extensions.get("httpStatusCode"))
                        .isEqualTo(ErrorCode.BIND_EXCEPTION.getStatus().value()));
    }

    @DisplayName("BindException 발생 시 Field Error가 있을 경우 확인할 수 있다.")
    @Test
    void handleBindExceptionFieldError() {
        // given
        BindException bindException = new BindException(new Object(), "target");
        bindException.addError(new FieldError("target", "username", "이름을 넣어주세요."));
        bindException.addError(new FieldError("target", "provider", "회원가입에 사용한 벤더사를 넣어주세요."));

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        // when
        GraphQLError graphQLError = bindExceptionHandler.handle(bindException, mockEnv);
        Map<String, Object> extensions = graphQLError.getExtensions();
        @SuppressWarnings("unchecked")
        List<Map<String, String>> fieldErrors = (List<Map<String, String>>) extensions.get("fieldErrors");

        // then
        assertAll(() -> assertThat(fieldErrors).hasSize(2),
                () -> assertThat(fieldErrors).containsExactlyInAnyOrder(
                        Map.of("field", "username", "message", "이름을 넣어주세요."),
                        Map.of("field", "provider", "message", "회원가입에 사용한 벤더사를 넣어주세요.")));
    }
}
