package com.loanpick.profile.resolver.result;

import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.ProfileColor;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import lombok.Builder;

//@formatter:off
@Builder
public record ProfileResult(
    Long id,
    PurposeOfLoan purposeOfLoan,
    EmploymentStatus employmentStatus,
    CreditGradeStatus creditGradeStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    Integer desiredLoanAmount,
    Integer seq,
    String profileName,
    ProfileColor profileColor
) {

    public static ProfileResult of(Profile profile) {
        return ProfileResult.builder()
            .id(profile.getId())
            .purposeOfLoan(profile.getPurposeOfLoan())
            .creditGradeStatus(profile.getCreditGradeStatus())
            .loanProductUsageCount(profile.getLoanProductUsageCount())
            .totalLoanUsageAmount(profile.getTotalLoanUsageAmount())
            .desiredLoanAmount(profile.getDesiredLoanAmount())
            .employmentStatus(profile.getEmploymentStatus())
            .profileName(profile.getProfileName())
            .seq(profile.getSeq())
            .profileColor(profile.getProfileColor())
            .build();
    }
}
