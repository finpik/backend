package com.loanpick.error.handler.impl;


import com.loanpick.error.enums.ErrorCode;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConstraintViolationExceptionHandlerTest {
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler = new ConstraintViolationExceptionHandler();

    @DisplayName("ConstraintViolationException 발생 시 GraphQLError로 변환한다")
    @Test
    void handleConstraintViolationException() {
        // given
        ConstraintViolation<?> mockViolation1 = mock(ConstraintViolation.class);
        Path mockPath1 = mock(Path.class);
        when(mockPath1.toString()).thenReturn("username");
        when(mockViolation1.getPropertyPath()).thenReturn(mockPath1);
        when(mockViolation1.getMessage()).thenReturn("이름을 넣어주세요.");

        ConstraintViolation<?> mockViolation2 = mock(ConstraintViolation.class);
        Path mockPath2 = mock(Path.class);
        when(mockPath2.toString()).thenReturn("provider");
        when(mockViolation2.getPropertyPath()).thenReturn(mockPath2);
        when(mockViolation2.getMessage()).thenReturn("회원가입에 사용한 벤더사를 넣어주세요.");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(
            Set.of(mockViolation1, mockViolation2)
        );

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        // mock Field 세팅
        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        // mock ExecutionStepInfo 세팅
        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        // when
        GraphQLError graphQLError = constraintViolationExceptionHandler.handle(constraintViolationException, mockEnv);
        Map<String, Object> extensions = graphQLError.getExtensions();

        // then
        assertAll(
            () -> assertThat(graphQLError.getMessage()).isEqualTo(constraintViolationException.getMessage()),
            () -> assertThat(extensions).isNotNull(),
            () -> assertThat(extensions.get("code")).isEqualTo(ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.getCode()),
            () -> assertThat(extensions.get("httpStatusErrorCode")).isEqualTo(ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.getStatus().name()),
            () -> assertThat(extensions.get("httpStatusCode")).isEqualTo(ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.getStatus().value())
        );
    }

    @DisplayName("ConstraintViolationException 발생 시 Field Error가 있을 경우 확인할 수 있다.")
    @Test
    void handleConstraintViolationExceptionFieldError() {
        //given
        // given
        ConstraintViolation<?> mockViolation1 = mock(ConstraintViolation.class);
        Path mockPath1 = mock(Path.class);
        when(mockPath1.toString()).thenReturn("username");
        when(mockViolation1.getPropertyPath()).thenReturn(mockPath1);
        when(mockViolation1.getMessage()).thenReturn("이름을 넣어주세요.");

        ConstraintViolation<?> mockViolation2 = mock(ConstraintViolation.class);
        Path mockPath2 = mock(Path.class);
        when(mockPath2.toString()).thenReturn("provider");
        when(mockViolation2.getPropertyPath()).thenReturn(mockPath2);
        when(mockViolation2.getMessage()).thenReturn("회원가입에 사용한 벤더사를 넣어주세요.");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(
            Set.of(mockViolation1, mockViolation2)
        );

        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        // mock Field 세팅
        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        // mock ExecutionStepInfo 세팅
        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        //when
        GraphQLError graphQLError = constraintViolationExceptionHandler.handle(constraintViolationException, mockEnv);
        Map<String, Object> extensions = graphQLError.getExtensions();
        @SuppressWarnings("unchecked")
        List<Map<String, String>> fieldErrors = (List<Map<String, String>>) extensions.get("fieldErrors");

        //then
        assertAll(
            ()-> assertThat(fieldErrors).hasSize(2),
            ()-> assertThat(fieldErrors).containsExactlyInAnyOrder(
                Map.of("field", "username", "message", "이름을 넣어주세요."),
                Map.of("field", "provider", "message", "회원가입에 사용한 벤더사를 넣어주세요.")
        ));
    }
}
