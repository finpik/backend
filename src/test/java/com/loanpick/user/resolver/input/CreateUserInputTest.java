package com.loanpick.user.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.loanpick.user.entity.Gender;
import com.loanpick.user.service.dto.CreateUserDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ActiveProfiles("test")
class CreateUserInputTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("유저 이름이 비었을 때 실패한다.")
    @Test
    void usernameBlank() {
        // given
        CreateUserInput input = new CreateUserInput("", // 1글자
                LocalDate.of(1990, 1, 1), Gender.MALE, "KAKAO", "123456");
        // when
        Set<ConstraintViolation<CreateUserInput>> violations = validator.validate(input);

        // then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @DisplayName("유저 이름이 1글자일 때 실패한다.")
    @Test
    void usernameOneLetter() {
        // given
        CreateUserInput input = new CreateUserInput("김", // 1글자
                LocalDate.of(1990, 1, 1), Gender.MALE, "KAKAO", "123456");

        // when
        Set<ConstraintViolation<CreateUserInput>> violations = validator.validate(input);

        // then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @DisplayName("생일이 null이면 실패한다.")
    @Test
    void birthdayBlank() {
        // given
        CreateUserInput input = new CreateUserInput("테스트", // 1글자
                null, Gender.MALE, "KAKAO", "123456");

        // when
        Set<ConstraintViolation<CreateUserInput>> violations = validator.validate(input);

        // then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth"));
    }

    @DisplayName("provider가 빈값일 경우 실패한다.")
    @Test
    void providerBlank() {
        // given
        CreateUserInput input = new CreateUserInput("김철수", LocalDate.of(1990, 1, 1), Gender.MALE, "", "123456");

        // when
        Set<ConstraintViolation<CreateUserInput>> violations = validator.validate(input);

        // then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("provider"));
    }

    @DisplayName("벤더에서 제공한 아이디가 비었을 때 실패한다.")
    @Test
    void idBlank() {
        // given
        CreateUserInput input = new CreateUserInput("김철수", LocalDate.of(1990, 1, 1), Gender.MALE, "", "");

        // when
        Set<ConstraintViolation<CreateUserInput>> violations = validator.validate(input);

        // then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("id"));
    }

    @DisplayName("input 데이터로 dto를 만들 수 있다.")
    @Test
    void toDto() {
        // given
        CreateUserInput input = new CreateUserInput("김철수", LocalDate.of(1990, 1, 1), Gender.MALE, "", "");

        // when
        CreateUserDto dto = input.toDto();

        // then
        assertThat(dto).isInstanceOf(CreateUserDto.class);
    }
}
