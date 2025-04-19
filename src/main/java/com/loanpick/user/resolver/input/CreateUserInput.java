package com.loanpick.user.resolver.input;

import com.loanpick.user.entity.Gender;

public record CreateUserInput(
        String name,
        String code
    ) {
}
