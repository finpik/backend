package com.loanpick.config.interceptor;

import static com.loanpick.util.Values.REFRESH_TOKEN;
import static com.loanpick.util.Values.USER;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;

import com.loanpick.user.entity.User;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GraphQLJwtInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        Object userAttr = request.getAttributes().get(USER);
        Object refreshTokenAttr = request.getAttributes().get(REFRESH_TOKEN);

        request.configureExecutionInput((exec, builder) -> builder.graphQLContext(ctx -> {
            if (userAttr instanceof User user) {
                ctx.put(USER, user);
            }
            if (refreshTokenAttr instanceof String refreshToken) {
                ctx.put(REFRESH_TOKEN, refreshToken);
            }
        }).build());

        return chain.next(request);
    }
}
