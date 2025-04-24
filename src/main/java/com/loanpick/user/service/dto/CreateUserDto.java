package com.loanpick.user.service.dto;

import java.time.LocalDate;

import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.RegistrationType;
import com.loanpick.user.entity.User;

import lombok.Builder;

@Builder
public record CreateUserDto(String username, LocalDate dateOfBirth, Gender gender, String provider, String id) {

    public User toEntity(String email) {
        return User.builder().username(username).dateOfBirth(dateOfBirth).gender(gender).email(email)
                .registrationType(RegistrationType.fromName(provider)).build();
    }
}
