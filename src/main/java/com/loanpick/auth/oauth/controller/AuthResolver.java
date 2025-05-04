package com.loanpick.auth.oauth.controller;

import static com.loanpick.util.Values.REFRESH_TOKEN;

import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.loanpick.auth.oauth.controller.result.AuthResult;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.auth.oauth.service.AuthService;
import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.user.entity.User;
import com.loanpick.user.service.UserService;

import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthResolver implements AuthApi {
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final UserService userService;

    @MutationMapping
    public AuthResult refresh(@ContextValue(REFRESH_TOKEN) String refreshToken, GraphQLContext context) {
        long userId = jwtProvider.getUserId(refreshToken);
        if (!authService.isValid(userId, refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        User user = userService.findUserBy(userId);

        String newRefreshToken = jwtProvider.createRefreshToken(user);

        authService.saveRefreshToken(userId, newRefreshToken);

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.setAttribute(REFRESH_TOKEN, newRefreshToken, RequestAttributes.SCOPE_REQUEST);
        }

        return new AuthResult(jwtProvider.createAccessToken(user));
    }
}
