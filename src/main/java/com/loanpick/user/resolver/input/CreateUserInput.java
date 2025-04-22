package com.loanpick.user.resolver.input;

import java.time.LocalDate;

import com.loanpick.user.entity.Gender;

public record CreateUserInput(String name, LocalDate dateOfBirth, Gender gender) {
}
