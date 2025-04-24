package com.loanpick.user.resolver;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.user.entity.User;
import com.loanpick.user.resolver.input.CreateUserInput;
import com.loanpick.user.resolver.result.CreateUserResult;
import com.loanpick.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserResolver implements UserApi {
    private final UserService userService;

    @Override
    @MutationMapping
    public CreateUserResult createUser(CreateUserInput input) {
        User user = userService.createUser(input.toDto());

        return CreateUserResult.of(user);
    }
}
