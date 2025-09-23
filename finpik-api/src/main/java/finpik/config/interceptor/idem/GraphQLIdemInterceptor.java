package finpik.config.interceptor.idem;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GraphQLIdemInterceptor implements WebGraphQlInterceptor {
    private final IdempotencyStore idempotencyStore;
    private static final String MUTATION = "mutation";
    private static final String QUERY = "query";
    private static final String IDEM_KEY = "IdemKey";
    private static final String ANONYMOUS = "anonymous";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String opName = request.getOperationName() != null ? request.getOperationName() : ANONYMOUS;
        String opType = request.getDocument().startsWith(MUTATION) ? MUTATION : QUERY;
        String document = request.getDocument();
        Map<String, Object> variables = request.getVariables();

        List<String> idemKey = request.getHeaders().get(IDEM_KEY);\

        if (opType == QUERY) {
            return chain.next(request);
        }

        return null;
    }
}
