package finpik.resolver.profile.application.dto;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import lombok.Getter;

@Getter
public class ProfileDto {
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

    public ProfileDto(Profile profile) {
        profileId = profile.getId();
        purposeOfLoan = profile.getPurposeOfLoan();
        occupation = profile.getOccupation();
        creditGradeStatus = profile.getCreditGradeStatus();
        loanProductUsageCount = profile.getLoanProductUsageCount();
        totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        desiredLoanAmount = profile.getDesiredLoanAmount();
        profileName = profile.getProfileName();
        profileColor = profile.getProfileColor();
        profileSeq = profile.getSeq();
    }
}
