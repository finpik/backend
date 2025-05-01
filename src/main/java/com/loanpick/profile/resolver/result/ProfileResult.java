package com.loanpick.profile.resolver.result;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

@Builder
public record ProfileResult(long id, PurposeOfLoan purposeOfLoan, EmploymentStatus employmentStatus,
        CreditGradeStatus creditGradeStatus, int loanProductUsageCount, int totalLoanUsageAmount, int desiredLoanAmount,
        int seq, String profileName) {

    public static ProfileResult of(Profile profile) {
        return ProfileResult.builder().id(profile.getId()).purposeOfLoan(profile.getPurposeOfLoan())
                .creditGradeStatus(profile.getCreditGradeStatus())
                .loanProductUsageCount(profile.getLoanProductUsageCount())
                .totalLoanUsageAmount(profile.getTotalLoanUsageAmount())
                .desiredLoanAmount(profile.getDesiredLoanAmount()).employmentStatus(profile.getEmploymentStatus())
                .profileName(profile.getProfileName()).seq(profile.getSeq()).build();
    }
}
