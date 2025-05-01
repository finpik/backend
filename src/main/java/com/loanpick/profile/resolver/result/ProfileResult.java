package com.loanpick.profile.resolver.result;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

@Builder
public record ProfileResult(PurposeOfLoan purposeOfLoan, EmploymentStatus employmentStatus,
        CreditGradeStatus creditGradeStatus, int loanProductUsageCount, int totalLoanUsageAmount, int desiredLoanAmount,
        int seq) {

    public static ProfileResult of(Profile profile) {
        return ProfileResult.builder().purposeOfLoan(profile.getPurposeOfLoan())
                .creditGradeStatus(profile.getCreditGradeStatus())
                .loanProductUsageCount(profile.getLoanProductUsageCount())
                .totalLoanUsageAmount(profile.getTotalLoanUsageAmount())
                .desiredLoanAmount(profile.getDesiredLoanAmount()).employmentStatus(profile.getEmploymentStatus())
                .seq(profile.getSeq()).build();
    }
}
