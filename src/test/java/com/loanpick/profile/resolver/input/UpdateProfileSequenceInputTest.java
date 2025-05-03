package com.loanpick.profile.resolver.input;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UpdateProfileSequenceInputTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("UpdateProfileSequenceInput 유효성 검증 - 개별 필드 실패 케이스")
    @MethodSource("invalidUpdateProfileSequenceInputFields")
    @ParameterizedTest(name = "[{index}] {1} 필드가 유효하지 않으면 검증 실패해야 한다")
    void validateInvalidFields(UpdateProfileSequenceInput input, String invalidField) {
        // given
        Set<ConstraintViolation<UpdateProfileSequenceInput>> violations = validator.validate(input);

        // when
        // then
        Assertions.assertThat(violations).as("필드: " + invalidField).isNotEmpty();
    }

    private static Stream<Arguments> invalidUpdateProfileSequenceInputFields() {
        return Stream.of(arguments(validInput().id(null).build(), "id"),
                arguments(validInput().seq(null).build(), "seq"));
    }

    private static UpdateProfileSequenceInput.UpdateProfileSequenceInputBuilder validInput() {
        return UpdateProfileSequenceInput.builder().id(1L).seq(0);
    }
}
