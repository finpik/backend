package com.loanpick.profile.service.dto;

import java.time.LocalDate;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.common.entity.enums.Occupation;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

@Builder
public record UpdateProfileDto(Long id, Occupation occupation, String workplaceName,
                               EmploymentForm employmentForm, Integer income, LocalDate employmentDate, PurposeOfLoan purposeOfLoan,
                               Integer desiredLoanAmount, LoanProductUsageStatus loanProductUsageStatus, Integer loanProductUsageCount,
                               Integer totalLoanUsageAmount, Integer creditScore, CreditGradeStatus creditGradeStatus, String profileName) {
    public Profile toEntity() {
        return Profile.builder().desiredLoanAmount(desiredLoanAmount).loanProductUsageStatus(loanProductUsageStatus)
                .totalLoanUsageAmount(totalLoanUsageAmount).creditScore(creditScore)
                .loanProductUsageCount(loanProductUsageCount).creditGradeStatus(creditGradeStatus)
                .creditGradeStatus(creditGradeStatus).loanProductUsageStatus(loanProductUsageStatus)
                .purposeOfLoan(purposeOfLoan).workplaceName(workplaceName).employmentForm(employmentForm).income(income)
                .profileName(profileName).employmentDate(employmentDate).occupation(occupation).build();
    }
}
