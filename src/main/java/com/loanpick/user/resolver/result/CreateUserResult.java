package com.loanpick.user.resolver.result;

import java.time.LocalDate;

import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.User;

import lombok.Builder;

@Builder
public record CreateUserResult(Long id, String username, LocalDate dateOfBirth, Gender gender, String token) {
    public static CreateUserResult of(User user, String token) {
        return CreateUserResult.builder().id(user.getId()).username(user.getUsername())
                .dateOfBirth(user.getDateOfBirth()).gender(user.getGender()).token(token).build();
    }
}
