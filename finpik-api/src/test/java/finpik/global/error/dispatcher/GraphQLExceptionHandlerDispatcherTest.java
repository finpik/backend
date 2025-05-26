package finpik.global.error.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.error.exception.BusinessException;
import finpik.global.error.handler.GraphQLExceptionHandler;
import finpik.global.error.handler.impl.BusinessExceptionHandler;
import finpik.global.error.handler.impl.DefaultExceptionHandler;

class GraphQLExceptionHandlerDispatcherTest {

    private final DefaultExceptionHandler defaultHandler = new DefaultExceptionHandler();

    private final GraphQLExceptionHandler<BusinessException> businessExceptionHandler = new BusinessExceptionHandler();

    private GraphQLExceptionHandlerDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new GraphQLExceptionHandlerDispatcher(List.of(businessExceptionHandler), defaultHandler);
    }

    @DisplayName("정확한 예외 타입에 대한 핸들러를 반환한다.")
    @Test
    void returnsSpecificHandler() {
        // given
        // when
        GraphQLExceptionHandler<?> handler = dispatcher.getHandler(BusinessException.class);

        // then
        assertThat(handler).isEqualTo(businessExceptionHandler);
    }

    @DisplayName("등록되지 않은 예외는 defaultHandler 반환한다.")
    @Test
    void returnsDefaultHandlerIfNoMatch() {
        // given
        // when
        GraphQLExceptionHandler<?> handler = dispatcher.getHandler(NullPointerException.class);

        // then
        assertThat(handler).isEqualTo(defaultHandler);
    }
}
