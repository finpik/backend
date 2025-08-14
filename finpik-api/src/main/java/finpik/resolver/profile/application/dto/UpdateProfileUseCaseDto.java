package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.BusinessType;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.policy.ProfileUpdateSpec;
import lombok.Builder;

@Builder
public record UpdateProfileUseCaseDto(
    Long profileId,
    Occupation occupation,
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
    LocalDate businessStartDate,
    BusinessType businessType
) {
    public ProfileUpdateSpec toSpec() {
        return new ProfileUpdateSpec(
            profileId, occupation, employmentForm,
            annualIncome, employmentDate, purposeOfLoan,
            desiredLoanAmount, loanProductUsageStatus,
            loanProductUsageCount, totalLoanUsageAmount,
            creditScore, creditGradeStatus, profileName,
            businessStartDate, businessType
        );
    }
}
