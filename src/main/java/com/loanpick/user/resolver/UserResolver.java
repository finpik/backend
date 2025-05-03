package com.loanpick.user.resolver;

import java.util.Date;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.user.entity.User;
import com.loanpick.user.resolver.input.CreateUserInput;
import com.loanpick.user.resolver.result.CreateUserResult;
import com.loanpick.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserResolver implements UserApi {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    @MutationMapping
    public CreateUserResult createUser(@Argument @Valid CreateUserInput input) {
        User user = userService.createUser(input.toDto());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 300_000);

        String token = jwtProvider.createToken(user, now, expiry);

        return CreateUserResult.of(user, token);
    }
}
