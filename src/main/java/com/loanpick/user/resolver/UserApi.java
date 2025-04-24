package com.loanpick.user.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import com.loanpick.user.resolver.input.CreateUserInput;
import com.loanpick.user.resolver.result.CreateUserResult;

import jakarta.validation.Valid;

@Controller
public interface UserApi {
    CreateUserResult createUser(@Argument @Valid CreateUserInput input);
}
