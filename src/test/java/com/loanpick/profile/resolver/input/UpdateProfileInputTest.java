package com.loanpick.profile.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UpdateProfileInputTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("UpdateProfileInput 유효성 검증 - 개별 필드 실패 케이스")
    @MethodSource("invalidUpdateProfileInputs")
    @ParameterizedTest(name = "[{index}] {1} 필드가 유효하지 않으면 검증 실패해야 한다")
    void validateInvalidFields(UpdateProfileInput input, String invalidField) {
        // given
        Set<ConstraintViolation<UpdateProfileInput>> violations = validator.validate(input);

        // when
        // then
        assertThat(violations).as("필드: " + invalidField).isNotEmpty();
    }

    private static Stream<Arguments> invalidUpdateProfileInputs() {
        return Stream.of(arguments(validBuilder().id(null).build(), "id"),
                arguments(validBuilder().employmentStatus(null).build(), "employmentStatus"),
                arguments(validBuilder().purposeOfLoan(null).build(), "purposeOfLoan"),
                arguments(validBuilder().desiredLoanAmount(-1).build(), "desiredLoanAmount"),
                arguments(validBuilder().loanProductUsageStatus(null).build(), "loanProductUsageStatus"),
                arguments(validBuilder().loanProductUsageCount(-1).build(), "loanProductUsageCount"),
                arguments(validBuilder().totalLoanUsageAmount(-1).build(), "totalLoanUsageAmount"),
                arguments(validBuilder().profileName("").build(), "profileName"),
                arguments(validBuilder().profileName("123456789012345").build(), "profileName") // too long
        );
    }

    private static UpdateProfileInput.UpdateProfileInputBuilder validBuilder() {
        return UpdateProfileInput.builder().id(1L).employmentStatus(EmploymentStatus.EMPLOYEE).workplaceName("회사")
                .employmentForm(EmploymentForm.FULL_TIME).income(50000000).employmentDate(LocalDate.of(2020, 1, 1))
                .purposeOfLoan(PurposeOfLoan.HOUSING).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(2)
                .totalLoanUsageAmount(15000000).creditScore(800).creditGradeStatus(CreditGradeStatus.UPPER)
                .profileName("프로필1");
    }
}
