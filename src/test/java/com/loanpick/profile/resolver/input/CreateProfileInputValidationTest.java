package com.loanpick.profile.resolver.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.common.entity.enums.Occupation;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateProfileInputValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private CreateProfileInput.CreateProfileInputBuilder validInputBuilder() {
        return CreateProfileInput.builder().employmentStatus(Occupation.EMPLOYEE).workplaceName("회사")
                .employmentForm(EmploymentForm.FULL_TIME).income(50000000).employmentDate(LocalDate.of(2020, 1, 1))
                .purposeOfLoan(PurposeOfLoan.LIVING_EXPENSES).desiredLoanAmount(10000000)
                .loanProductUsageStatus(LoanProductUsageStatus.USING).loanProductUsageCount(1)
                .totalLoanUsageAmount(10000000).creditScore(800).creditGradeStatus(CreditGradeStatus.UPPER)
                .profileName("내 프로필");
    }

    @DisplayName("employmentStatus가 null이면 실패한다")
    @Test
    void validateEmploymentStatus_null() {
        CreateProfileInput input = validInputBuilder().employmentStatus(null).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("employmentStatus"));
    }

    @DisplayName("purposeOfLoan이 null이면 실패한다")
    @Test
    void validatePurposeOfLoan_null() {
        CreateProfileInput input = validInputBuilder().purposeOfLoan(null).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("purposeOfLoan"));
    }

    @DisplayName("desiredLoanAmount가 0 미만이면 실패한다")
    @Test
    void validateDesiredLoanAmount_negative() {
        CreateProfileInput input = validInputBuilder().desiredLoanAmount(-1000).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("desiredLoanAmount"));
    }

    @DisplayName("loanProductUsageStatus가 null이면 실패한다")
    @Test
    void validateLoanProductUsageStatus_null() {
        CreateProfileInput input = validInputBuilder().loanProductUsageStatus(null).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("loanProductUsageStatus"));
    }

    @DisplayName("loanProductUsageCount가 0 미만이면 실패한다")
    @Test
    void validateLoanProductUsageCount_negative() {
        CreateProfileInput input = validInputBuilder().loanProductUsageCount(-1).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("loanProductUsageCount"));
    }

    @DisplayName("totalLoanUsageAmount가 0 미만이면 실패한다")
    @Test
    void validateTotalLoanUsageAmount_negative() {
        CreateProfileInput input = validInputBuilder().totalLoanUsageAmount(-5000).build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("totalLoanUsageAmount"));
    }

    @DisplayName("profileName이 blank면 실패한다")
    @Test
    void validateProfileName_blank() {
        CreateProfileInput input = validInputBuilder().profileName("").build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("profileName"));
    }

    @DisplayName("profileName이 14자를 초과하면 실패한다")
    @Test
    void validateProfileName_tooLong() {
        CreateProfileInput input = validInputBuilder().profileName("123456789012345") // 15자
                .build();

        Set<ConstraintViolation<CreateProfileInput>> violations = validator.validate(input);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("profileName"));
    }
}
