package com.loanpick.profile.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.loanpick.profile.entity.enums.ProfileColor;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UpdateProfileColorInputTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("UpdateProfileColorInput 유효성 검증 - 개별 필드 실패 케이스")
    @MethodSource("invalidUpdateProfileColorInput")
    @ParameterizedTest(name = "[{index}] {1} 필드가 유효하지 않으면 검증 실패해야 한다")
    void validateInvalidFields(UpdateProfileColorInput input, String invalidField) {
        // given
        Set<ConstraintViolation<UpdateProfileColorInput>> violations = validator.validate(input);

        // when
        // then
        assertThat(violations).as("필드: " + invalidField).isNotEmpty();
    }

    private static Stream<Arguments> invalidUpdateProfileColorInput() {
        return Stream.of(arguments(validBuilder().id(null).build(), "id"),
                arguments(validBuilder().profileColor(null).build(), "profileColor"));
    }

    private static UpdateProfileColorInput.UpdateProfileColorInputBuilder validBuilder() {
        return UpdateProfileColorInput.builder().id(1L).profileColor(ProfileColor.GRAY_TWO);
    }
}
