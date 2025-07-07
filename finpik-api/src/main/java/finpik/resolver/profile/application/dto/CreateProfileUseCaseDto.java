package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import finpik.profile.entity.policy.ProfileCreationSpec;
import finpik.user.entity.User;
import lombok.Builder;

@Builder
public record CreateProfileUseCaseDto(
    EmploymentForm employmentForm,
    Integer annualIncome,
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
    Long userId,
    ProfileColor profileColor,
    LocalDate businessStartDate
) {
    public Profile toDomain(User user) {
        ProfileCreationSpec spec = ProfileCreationSpec.createNew(
            desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            profileColor, annualIncome, businessStartDate,
            employmentDate, occupation,
            employmentForm, user
        );

        return Profile.of(spec);
    }
}
