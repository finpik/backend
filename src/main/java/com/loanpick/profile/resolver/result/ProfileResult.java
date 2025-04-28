package com.loanpick.profile.resolver.result;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

@Builder
public record ProfileResult(PurposeOfLoan purposeOfLoan, EmploymentStatus employmentStatus,
        CreditGradeStatus creditGradeStatus, int loanProductUsageCount, int totalLoanUsageAmount,
        int desiredLoanAmount) {
}
