package finpik.global.error.dispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import finpik.global.error.handler.GraphQLExceptionHandler;
import finpik.global.error.handler.impl.DefaultExceptionHandler;

@Component
public class GraphQLExceptionHandlerDispatcher {

    private final Map<Class<? extends Throwable>, GraphQLExceptionHandler<? extends Throwable>> handlerMap = new HashMap<>();
    private final DefaultExceptionHandler defaultExceptionHandler;

    public GraphQLExceptionHandlerDispatcher(
        List<GraphQLExceptionHandler<? extends Throwable>> handlers,
        DefaultExceptionHandler defaultExceptionHandler
    ) {
        this.defaultExceptionHandler = defaultExceptionHandler;
        for (GraphQLExceptionHandler<? extends Throwable> handler : handlers) {
            handlerMap.put(handler.supportedExceptionType(), handler);
        }
    }

    public <T extends Throwable> GraphQLExceptionHandler<T> getHandler(Class<T> exceptionType) {
        Class<?> current = exceptionType;

        while (current != null && Throwable.class.isAssignableFrom(current)) {
            GraphQLExceptionHandler<?> handler = handlerMap.get(current);
            if (handler != null) {
                return castHandler(handler);
            }
            current = current.getSuperclass();
        }

        return castHandler(defaultExceptionHandler);
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> GraphQLExceptionHandler<T> castHandler(GraphQLExceptionHandler<?> handler) {
        return (GraphQLExceptionHandler<T>) handler;
    }
}
