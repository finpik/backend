package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import lombok.Builder;

@Builder
public record UpdateProfileUseCaseDto(
    Long id,
    Occupation occupation,
    String workplaceName,
    EmploymentForm employmentForm,
    Integer income,
    LocalDate employmentDate,
    PurposeOfLoan purposeOfLoan,
    Integer desiredLoanAmount,
    LoanProductUsageStatus loanProductUsageStatus,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    Integer creditScore,
    CreditGradeStatus creditGradeStatus,
    String profileName
) {
    public Profile toDomain() {
        return Profile.withId(
            id, desiredLoanAmount, loanProductUsageCount, totalLoanUsageAmount,
            creditScore, creditGradeStatus, income, null,
            workplaceName, employmentForm, loanProductUsageStatus,
            purposeOfLoan, employmentDate, profileName,
            occupation, null, null
        );
    }
}
