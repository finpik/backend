package com.loanpick.profile.service.dto;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateProfileDto(
    String workplaceName, EmploymentForm employmentForm, int income, EmploymentStatus employmentStatus,
    LocalDate employmentDate, LoanProductUsageStatus loanProductUsageStatus, int loanProductUsageCount,
    int totalLoanUsageAmount, PurposeOfLoan purposeOfLoan, int desiredLoanAmount,
    CreditGradeStatus creditGradeStatus, int creditScore, String profileName
) {
    public Profile toEntity() {
        return Profile.builder()
            .desiredLoanAmount(desiredLoanAmount)
            .loanProductUsageStatus(loanProductUsageStatus)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .creditScore(creditScore)
            .creditGradeStatus(creditGradeStatus)
            .loanProductUsageStatus(loanProductUsageStatus)
            .purposeOfLoan(purposeOfLoan)
            .workplaceName(workplaceName)
            .employmentForm(employmentForm)
            .income(income)
            .profileName(profileName)
            .employmentDate(employmentDate)
            .employmentStatus(employmentStatus)
            .build();
    }
}
