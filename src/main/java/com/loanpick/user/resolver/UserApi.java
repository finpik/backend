package com.loanpick.user.resolver;

import com.loanpick.user.resolver.input.CreateUserInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

@Controller
public interface UserApi {
    void createUser(@Argument CreateUserInput input);
}
