package com.loanpick.profile.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;

import com.loanpick.common.entity.enums.Occupation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
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
        String profileName = "내 프로필";
        EmploymentForm employmentForm = EmploymentForm.FULL_TIME;
        LoanProductUsageStatus loanProductUsageStatus = LoanProductUsageStatus.USING;
        PurposeOfLoan purposeOfLoan = PurposeOfLoan.HOUSING;
        Occupation occupation = Occupation.SELF_EMPLOYED;
        CreditGradeStatus creditGradeStatus = CreditGradeStatus.UPPER;

        // when
        Profile profile = Profile.builder().desiredLoanAmount(desiredLoanAmount)
                .loanProductUsageCount(loanProductUsageCount).totalLoanUsageAmount(totalLoanUsageAmount)
                .creditScore(creditScore).profileName(profileName).employmentForm(employmentForm)
                .loanProductUsageStatus(loanProductUsageStatus).purposeOfLoan(purposeOfLoan)
                .employmentStatus(occupation).creditGradeStatus(creditGradeStatus).build();

        // then
        assertAll(() -> assertThat(profile.getDesiredLoanAmount()).isEqualTo(desiredLoanAmount),
                () -> assertThat(profile.getLoanProductUsageCount()).isEqualTo(loanProductUsageCount),
                () -> assertThat(profile.getTotalLoanUsageAmount()).isEqualTo(totalLoanUsageAmount),
                () -> assertThat(profile.getCreditScore()).isEqualTo(creditScore),
                () -> assertThat(profile.getProfileName()).isEqualTo(profileName),
                () -> assertThat(profile.getEmploymentForm()).isEqualTo(employmentForm),
                () -> assertThat(profile.getLoanProductUsageStatus()).isEqualTo(loanProductUsageStatus),
                () -> assertThat(profile.getPurposeOfLoan()).isEqualTo(purposeOfLoan),
                () -> assertThat(profile.getOccupation()).isEqualTo(occupation),
                () -> assertThat(profile.getCreditGradeStatus()).isEqualTo(creditGradeStatus));
    }

    @DisplayName("프로필을 만들 때 EmploymentStatus가 EMPLOYEE일 때, 관련 필드가 없으면 에러가 발생한다.")
    @Test
    void validateInfoRelatedEmploymentStatus() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Profile.builder().employmentStatus(Occupation.EMPLOYEE)
                .workplaceName("Sample Company").employmentDate(LocalDate.of(2020, 1, 1)).build())
                .hasMessage(ErrorCode.INVALID_EMPLOYMENT_INFO.getMessage());
    }
}
