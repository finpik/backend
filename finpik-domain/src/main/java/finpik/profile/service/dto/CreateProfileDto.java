package finpik.profile.service.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import finpik.user.entity.User;
import lombok.Builder;

//@formatter:off
@Builder
public record CreateProfileDto(
    String workplaceName,
    EmploymentForm employmentForm,
    Integer income,
    Occupation occupation,
    LocalDate employmentDate,
    LoanProductUsageStatus loanProductUsageStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    PurposeOfLoan purposeOfLoan,
    Integer desiredLoanAmount,
    CreditGradeStatus creditGradeStatus,
    Integer creditScore,
    String profileName,
    ProfileColor profileColor,
    User user
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
            .occupation(occupation)
            .user(user)
            .profileColor(profileColor)
            .build();
    }
}
