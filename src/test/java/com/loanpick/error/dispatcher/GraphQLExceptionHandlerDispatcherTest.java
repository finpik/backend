package com.loanpick.error.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.loanpick.error.handler.GraphQLExceptionHandler;
import com.loanpick.error.handler.impl.DefaultExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class GraphQLExceptionHandlerDispatcherTest {

    @DisplayName("등록된 예외 핸들러가 있으면 해당 핸들러를 반환한다")
    @Test
    void getHandler_whenHandlerRegistered() {
        // given
        @SuppressWarnings("unchecked")
        GraphQLExceptionHandler<ConstraintViolationException> constraintHandler =
            (GraphQLExceptionHandler<ConstraintViolationException>) mock(GraphQLExceptionHandler.class);

        DefaultExceptionHandler defaultHandler = mock(DefaultExceptionHandler.class);

        when(constraintHandler.supportedExceptionType()).thenReturn(ConstraintViolationException.class);

        GraphQLExceptionHandlerDispatcher dispatcher = new GraphQLExceptionHandlerDispatcher(
            List.of(constraintHandler), defaultHandler
        );

        // when
        GraphQLExceptionHandler<ConstraintViolationException> handler = dispatcher.getHandler(ConstraintViolationException.class);

        // then
        assertThat(handler).isEqualTo(constraintHandler);
    }

    @DisplayName("등록된 핸들러가 없으면 DefaultExceptionHandler를 반환한다")
    @Test
    void getHandler_whenNoHandlerRegistered() {
        // given
        DefaultExceptionHandler defaultHandler = mock(DefaultExceptionHandler.class);
        GraphQLExceptionHandlerDispatcher dispatcher = new GraphQLExceptionHandlerDispatcher(
            List.of(), defaultHandler
        );

        // when
        GraphQLExceptionHandler<Throwable> handler = dispatcher.getHandler(Throwable.class);

        // then
        assertThat(handler).isEqualTo(defaultHandler);
    }

    @DisplayName("등록된 하위 타입 핸들러가 없고 부모 타입 핸들러가 있으면 부모 핸들러를 반환한다")
    @Test
    void getHandler_whenParentHandlerExists() {
        // given
        @SuppressWarnings("unchecked")
        GraphQLExceptionHandler<RuntimeException> runtimeHandler =
            (GraphQLExceptionHandler<RuntimeException>) mock(GraphQLExceptionHandler.class);

        DefaultExceptionHandler defaultHandler = mock(DefaultExceptionHandler.class);

        when(runtimeHandler.supportedExceptionType()).thenReturn(RuntimeException.class);

        GraphQLExceptionHandlerDispatcher dispatcher = new GraphQLExceptionHandlerDispatcher(
            List.of(runtimeHandler), defaultHandler
        );

        // when
        GraphQLExceptionHandler<IllegalStateException> handler = dispatcher.getHandler(IllegalStateException.class);

        // then
        assertThat(handler).isEqualTo(runtimeHandler);
    }
}
