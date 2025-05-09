package com.loanpick.profile.service.dto;

import java.time.LocalDate;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.ProfileColor;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.user.entity.User;

import lombok.Builder;

//@formatter:off
@Builder
public record CreateProfileDto(
    String workplaceName,
    EmploymentForm employmentForm,
    Integer income,
    EmploymentStatus employmentStatus,
    LocalDate employmentDate,
    LoanProductUsageStatus loanProductUsageStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    PurposeOfLoan purposeOfLoan,
    Integer desiredLoanAmount,
    CreditGradeStatus creditGradeStatus,
    Integer creditScore,
    String profileName,
    User user,
    ProfileColor profileColor
) {
    public Profile toEntity() {
        return Profile.builder()
            .desiredLoanAmount(desiredLoanAmount)
            .loanProductUsageStatus(loanProductUsageStatus)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .creditScore(creditScore)
            .loanProductUsageCount(loanProductUsageCount)
            .creditGradeStatus(creditGradeStatus)
            .creditGradeStatus(creditGradeStatus)
            .loanProductUsageStatus(loanProductUsageStatus)
            .purposeOfLoan(purposeOfLoan)
            .workplaceName(workplaceName)
            .employmentForm(employmentForm)
            .income(income)
            .profileName(profileName)
            .employmentDate(employmentDate)
            .employmentStatus(employmentStatus)
            .user(user)
            .profileColor(profileColor)
            .build();
    }
}
