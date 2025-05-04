package com.loanpick.auth.oauth.controller;

import static com.loanpick.util.Values.REFRESH_TOKEN;

import org.springframework.web.bind.annotation.CookieValue;

import com.loanpick.auth.oauth.controller.result.AuthResult;

import graphql.GraphQLContext;

public interface AuthApi {
    AuthResult refresh(@CookieValue(REFRESH_TOKEN) String refreshToken, GraphQLContext context);
}
