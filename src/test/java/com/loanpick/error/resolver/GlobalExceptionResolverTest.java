package com.loanpick.error.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.error.dispatcher.GraphQLExceptionHandlerDispatcher;
import com.loanpick.error.handler.GraphQLExceptionHandler;

import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;

class GlobalExceptionResolverTest {

    @DisplayName("등록된 예외가 발생하면 등록된 핸들러로 처리한다")
    @Test
    void resolveToSingleError_success() {
        // given
        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("createUser")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        ConstraintViolationException ex = new ConstraintViolationException(Set.of());

        @SuppressWarnings("unchecked")
        GraphQLExceptionHandler<ConstraintViolationException> mockHandler = (GraphQLExceptionHandler<ConstraintViolationException>) mock(
                GraphQLExceptionHandler.class);

        GraphQLError mockGraphQLError = mock(GraphQLError.class);
        when(mockHandler.handle(ex, mockEnv)).thenReturn(mockGraphQLError);

        GraphQLExceptionHandlerDispatcher mockDispatcher = mock(GraphQLExceptionHandlerDispatcher.class);
        when(mockDispatcher.getHandler(ConstraintViolationException.class)).thenReturn(mockHandler);

        GlobalExceptionResolver resolver = new GlobalExceptionResolver(mockDispatcher);

        // when
        GraphQLError result = resolver.resolveToSingleError(ex, mockEnv);

        // then
        assertThat(result).isEqualTo(mockGraphQLError);
    }

    @DisplayName("등록된 핸들러가 없는 경우 DefaultExceptionHandler가 처리한다.")
    @Test
    void resolveToSingleError_noHandler() {
        // given
        DataFetchingEnvironment mockEnv = mock(DataFetchingEnvironment.class);

        Field mockField = mock(Field.class);
        when(mockField.getSourceLocation()).thenReturn(SourceLocation.EMPTY);
        when(mockEnv.getField()).thenReturn(mockField);

        ExecutionStepInfo mockExecutionStepInfo = mock(ExecutionStepInfo.class);
        when(mockExecutionStepInfo.getPath()).thenReturn(ResultPath.fromList(List.of("someMutation")));
        when(mockEnv.getExecutionStepInfo()).thenReturn(mockExecutionStepInfo);

        Throwable ex = new Throwable("핸들러가 없음");

        GraphQLExceptionHandlerDispatcher mockDispatcher = mock(GraphQLExceptionHandlerDispatcher.class);

        @SuppressWarnings("unchecked")
        GraphQLExceptionHandler<Throwable> defaultHandler = (GraphQLExceptionHandler<Throwable>) mock(
                GraphQLExceptionHandler.class);
        GraphQLError mockGraphQLError = mock(GraphQLError.class);

        when(mockDispatcher.getHandler(Throwable.class)).thenReturn(defaultHandler);
        when(defaultHandler.handle(ex, mockEnv)).thenReturn(mockGraphQLError);

        GlobalExceptionResolver resolver = new GlobalExceptionResolver(mockDispatcher);

        // when
        GraphQLError result = resolver.resolveToSingleError(ex, mockEnv);

        // then
        assertThat(result).isEqualTo(mockGraphQLError);
    }
}
