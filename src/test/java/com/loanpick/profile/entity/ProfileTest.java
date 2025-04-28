package com.loanpick.profile.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

class ProfileTest {

    @DisplayName("Profile 객체 생성 시 필드가 정상적으로 매핑된다")
    @Test
    void createProfile_success() {
        // given
        int desiredLoanAmount = 10000000;
        int loanProductUsageCount = 2;
        int totalLoanUsageAmount = 5000000;
        int creditScore = 750;
        int income = 60000000;
        String workplaceName = "Sample Company";
        String profileName = "내 프로필";
        EmploymentForm employmentForm = EmploymentForm.FULL_TIME;
        LoanProductUsageStatus loanProductUsageStatus = LoanProductUsageStatus.USING;
        PurposeOfLoan purposeOfLoan = PurposeOfLoan.HOUSING;
        EmploymentStatus employmentStatus = EmploymentStatus.EMPLOYEE;
        CreditGradeStatus creditGradeStatus = CreditGradeStatus.UPPER;
        LocalDate employmentDate = LocalDate.of(2020, 1, 1);

        // when
        Profile profile = Profile.builder().desiredLoanAmount(desiredLoanAmount)
                .loanProductUsageCount(loanProductUsageCount).totalLoanUsageAmount(totalLoanUsageAmount)
                .creditScore(creditScore).income(income).workplaceName(workplaceName).profileName(profileName)
                .employmentForm(employmentForm).loanProductUsageStatus(loanProductUsageStatus)
                .purposeOfLoan(purposeOfLoan).employmentStatus(employmentStatus).creditGradeStatus(creditGradeStatus)
                .employmentDate(employmentDate).build();

        // then
        assertAll(() -> assertThat(profile.getDesiredLoanAmount()).isEqualTo(desiredLoanAmount),
                () -> assertThat(profile.getLoanProductUsageCount()).isEqualTo(loanProductUsageCount),
                () -> assertThat(profile.getTotalLoanUsageAmount()).isEqualTo(totalLoanUsageAmount),
                () -> assertThat(profile.getCreditScore()).isEqualTo(creditScore),
                () -> assertThat(profile.getIncome()).isEqualTo(income),
                () -> assertThat(profile.getWorkplaceName()).isEqualTo(workplaceName),
                () -> assertThat(profile.getProfileName()).isEqualTo(profileName),
                () -> assertThat(profile.getEmploymentForm()).isEqualTo(employmentForm),
                () -> assertThat(profile.getLoanProductUsageStatus()).isEqualTo(loanProductUsageStatus),
                () -> assertThat(profile.getPurposeOfLoan()).isEqualTo(purposeOfLoan),
                () -> assertThat(profile.getEmploymentStatus()).isEqualTo(employmentStatus),
                () -> assertThat(profile.getCreditGradeStatus()).isEqualTo(creditGradeStatus),
                () -> assertThat(profile.getEmploymentDate()).isEqualTo(employmentDate));
    }
}
