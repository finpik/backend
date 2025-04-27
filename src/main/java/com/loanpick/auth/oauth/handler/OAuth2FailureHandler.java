package com.loanpick.auth.oauth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.loanpick.auth.oauth.constants.ErrorMessage;
import com.loanpick.auth.oauth.constants.HttpHeaderValues;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(HttpHeaderValues.APPLICATION_JSON);
        response.setCharacterEncoding(HttpHeaderValues.UTF_8);
        response.getWriter().write(ErrorMessage.AUTHORIZATION_FAIL);
    }
}
