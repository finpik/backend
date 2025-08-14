package finpik.resolver.profile.application.dto;

import finpik.entity.Profile;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import lombok.Getter;

@Getter
public class ProfileResultDto {
    Long profileId;
    PurposeOfLoan purposeOfLoan;
    Occupation occupation;
    CreditGradeStatus creditGradeStatus;
    Integer loanProductUsageCount;
    Integer totalLoanUsageAmount;
    Integer desiredLoanAmount;
    String profileName;
    Integer profileSeq;
    ProfileColor profileColor;
    Integer annualIncome;

    public ProfileResultDto(Profile profile) {
        profileId = profile.getId();
        purposeOfLoan = profile.getPurposeOfLoan();
        occupation = profile.getOccupationDetail().getOccupation();
        creditGradeStatus = profile.getCreditScore().creditGradeStatus();
        loanProductUsageCount = profile.getLoanProductUsageCount();
        totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        desiredLoanAmount = profile.getDesiredLoanAmount();
        profileName = profile.getProfileName();
        profileColor = profile.getProfileColor();
        profileSeq = profile.getSeq();
        annualIncome = profile.getOccupationDetail().getAnnualIncome();
    }
}
