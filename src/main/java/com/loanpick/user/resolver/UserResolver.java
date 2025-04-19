package com.loanpick.user.resolver;

import com.loanpick.user.resolver.input.CreateUserInput;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserResolver implements UserApi {

    @Override
    @MutationMapping
    public void createUser(CreateUserInput input) {

    }
}
