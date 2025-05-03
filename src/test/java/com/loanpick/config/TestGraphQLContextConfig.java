package com.loanpick.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.server.WebGraphQlInterceptor;

import com.loanpick.user.entity.User;

@TestConfiguration
public class TestGraphQLContextConfig {

    @Bean
    public WebGraphQlInterceptor testUserInjector() {
        return (request, chain) -> {
            User mockUser = User.builder().id(99L).username("testUser").build();

            request.configureExecutionInput(
                    (exec, builder) -> builder.graphQLContext(ctx -> ctx.put("user", mockUser)).build());
            return chain.next(request);
        };
    }
}
