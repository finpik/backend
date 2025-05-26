package finpik.auth.controller;

import static finpik.util.Values.REFRESH_TOKEN;

import org.springframework.web.bind.annotation.CookieValue;

import finpik.auth.controller.result.AuthResult;
import graphql.GraphQLContext;

public interface AuthApi {
    AuthResult refresh(@CookieValue(REFRESH_TOKEN) String refreshToken, GraphQLContext context);
}
