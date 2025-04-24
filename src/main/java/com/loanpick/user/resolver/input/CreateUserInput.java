package com.loanpick.user.resolver.input;

import java.time.LocalDate;

import com.loanpick.user.entity.Gender;
import com.loanpick.user.service.dto.CreateUserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserInput(
        @NotBlank(message = "이름을 넣어주세요.") @Size(min = 2, message = "이름은 최소 2글자 이상이어야 합니다.") String username,
        @NotNull(message = "생일을 넣어주세요.") LocalDate dateOfBirth, Gender gender,
        @NotBlank(message = "회원가입에 사용한 벤더사를 넣어주세요.") String provider,
        @NotBlank(message = "사용자 벤더 아이디를 넣어주세요.") String id) {

    public CreateUserDto toDto() {
        return CreateUserDto.builder().username(username).dateOfBirth(dateOfBirth).gender(gender).provider(provider)
                .id(id).build();
    }
}
