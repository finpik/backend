package entity.policy;

import finpik.entity.User;
import finpik.entity.enums.*;
import finpik.entity.policy.ProfileCreationSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProfileCreationSpecTest {

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        //given
        Long id = 1L;
        Integer desiredLoanAmount = 5000;
        Integer loanProductUsageCount = 2;
        Integer totalLoanUsageAmount = 15000;
        Integer creditScore = 850;
        String profileName = "테스트 프로필";
        Integer seq = 1;
        CreditGradeStatus creditGradeStatus = CreditGradeStatus.GOOD;
        LoanProductUsageStatus loanProductUsageStatus = LoanProductUsageStatus.USING;
        PurposeOfLoan purposeOfLoan = PurposeOfLoan.LOAN_REPAYMENT;
        ProfileColor profileColor = ProfileColor.GRAY;
        Integer annualIncome = 4500;
        LocalDate businessStartDate = LocalDate.of(2020, 1, 1);
        LocalDate employmentDate = LocalDate.of(2022, 6, 1);
        Occupation occupation = Occupation.EMPLOYEE;
        EmploymentForm employmentForm = EmploymentForm.FULL_TIME;
        User user = new User(1L, "username", "user@example.com", Gender.MALE, RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(1995, 1, 1));
        Float minInterestRate = 0.0f;
        Integer recommendedLoanProductCount = 0;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        BusinessType businessType = null;

        //when
        ProfileCreationSpec spec = new ProfileCreationSpec(
            id, desiredLoanAmount, loanProductUsageCount, totalLoanUsageAmount,
            creditScore, profileName, seq, creditGradeStatus, loanProductUsageStatus,
            purposeOfLoan, profileColor, annualIncome, businessStartDate, employmentDate,
            occupation, businessType, employmentForm, user,
            recommendedLoanProductCount, minInterestRate, createdAt, updatedAt
        );

        //then
        assertAll(
            () -> assertThat(spec).isNotNull(),
            () -> assertThat(spec.profileName()).isEqualTo("테스트 프로필"),
            () -> assertThat(spec.user().getEmail()).isEqualTo("user@example.com")
        );
    }
}
