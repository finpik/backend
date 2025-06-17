package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import finpik.profile.entity.policy.ProfileCreationSpec;
import lombok.Builder;

@Builder
public record UpdateProfileUseCaseDto(
    Long id,
    Occupation occupation,
    String workplaceName,
    EmploymentForm employmentForm,
    Integer annualIncome,
    LocalDate employmentDate,
    PurposeOfLoan purposeOfLoan,
    Integer desiredLoanAmount,
    LoanProductUsageStatus loanProductUsageStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    Integer creditScore,
    CreditGradeStatus creditGradeStatus,
    String profileName,
    LocalDate businessStartDate
) {
    public Profile toDomain() {
        ProfileCreationSpec spec = ProfileCreationSpec.createNew(
            desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            null, annualIncome, businessStartDate,
            employmentDate, occupation,
            employmentForm, null
        );

        return Profile.withId(spec);
    }
}
