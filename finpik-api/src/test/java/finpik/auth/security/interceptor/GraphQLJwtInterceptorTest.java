package finpik.auth.security.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.ExecutionGraphQlResponse;
import org.springframework.graphql.GraphQlRequest;
import org.springframework.graphql.server.WebGraphQlInterceptor.Chain;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.support.DefaultExecutionGraphQlRequest;
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import finpik.config.interceptor.GraphQLJwtInterceptor;
import finpik.user.entity.User;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQLContext;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GraphQLJwtInterceptorTest {

    private GraphQLJwtInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new GraphQLJwtInterceptor();
    }

    @DisplayName("USER와 REFRESH_TOKEN이 request attributes에 있으면 GraphQLContext에 추가된다")
    @Test
    void addsUserAndRefreshTokenToGraphQLContext() {
        // given
        User user = User.builder().id(1L).email("test@example.com").build();
        String refreshToken = "test-refresh-token";

        Map<String, Object> attributes = Map.of("user", user, "refreshToken", refreshToken);

        String query = "query { test }";

        GraphQlRequest body = new DefaultExecutionGraphQlRequest(query, null, null, null, "id", null);

        URI uri = UriComponentsBuilder.fromPath("/graphql").build().toUri();

        WebGraphQlRequest request = new WebGraphQlRequest(uri, new HttpHeaders(), new LinkedMultiValueMap<>(), // cookies
                new InetSocketAddress("localhost", 8080), attributes, body, "1", Locale.getDefault());

        Chain chain = req -> {
            ExecutionInput input = req.toExecutionInput();
            GraphQLContext ctx = input.getGraphQLContext();

            // then
            assertThat(ctx.<User>get("user")).isEqualTo(user);
            assertThat(ctx.<String>get("refreshToken")).isEqualTo(refreshToken);

            ExecutionResult executionResult = ExecutionResultImpl.newExecutionResult().data(Map.of("dummy", "result"))
                    .build();

            ExecutionGraphQlResponse dummyResponse = new DefaultExecutionGraphQlResponse(input, executionResult);

            // Return dummy response
            return Mono.just(new WebGraphQlResponse(dummyResponse));
        };

        // when
        interceptor.intercept(request, chain).block();
    }

    @Test
    @DisplayName("속성이 없으면 GraphQLContext에 아무 것도 추가하지 않는다")
    void skipsWhenAttributesAreMissing() {
        // given
        String query = "query { test }";

        GraphQlRequest body = new DefaultExecutionGraphQlRequest(query, null, null, null, "id", null);

        WebGraphQlRequest request = new WebGraphQlRequest(URI.create("/graphql"), new HttpHeaders(), null, null,
                Map.of(), body, "id", Locale.getDefault());

        // then
        Chain chain = req -> {
            ExecutionInput input = req.toExecutionInput();
            GraphQLContext ctx = input.getGraphQLContext();

            assertThat(ctx.<User>get("user")).isNull();
            assertThat(ctx.<String>get("refreshToken")).isNull();

            ExecutionResult executionResult = ExecutionResultImpl.newExecutionResult().data(Map.of("dummy", "result"))
                    .build();

            ExecutionGraphQlResponse dummyResponse = new DefaultExecutionGraphQlResponse(input, executionResult);

            // Return dummy response
            return Mono.just(new WebGraphQlResponse(dummyResponse));
        };

        // when
        interceptor.intercept(request, chain).block();
    }
}
