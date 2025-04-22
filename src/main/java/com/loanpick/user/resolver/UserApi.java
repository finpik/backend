package com.loanpick.user.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import com.loanpick.user.resolver.input.CreateUserInput;

@Controller
public interface UserApi {
    void createUser(@Argument CreateUserInput input);
}
