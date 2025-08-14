package finpik.resolver.profile.resolver.result;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.resolver.profile.application.dto.ProfileResultDto;
import lombok.Builder;

@Builder
public record ProfileResult(
    Long profileId,
    PurposeOfLoan purposeOfLoan,
    Occupation occupation,
    CreditGradeStatus creditGradeStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    Integer desiredLoanAmount,
    Integer profileSeq,
    String profileName,
    ProfileColor profileColor,
    Integer annualIncome
) {

    public static ProfileResult of(ProfileResultDto profile) {
        return ProfileResult.builder()
            .profileId(profile.getProfileId())
            .purposeOfLoan(profile.getPurposeOfLoan())
            .creditGradeStatus(profile.getCreditGradeStatus())
            .loanProductUsageCount(profile.getLoanProductUsageCount())
            .totalLoanUsageAmount(profile.getTotalLoanUsageAmount())
            .desiredLoanAmount(profile.getDesiredLoanAmount())
            .occupation(profile.getOccupation())
            .profileName(profile.getProfileName())
            .profileSeq(profile.getProfileSeq())
            .profileColor(profile.getProfileColor())
            .annualIncome(profile.getAnnualIncome())
            .build();
    }
}
