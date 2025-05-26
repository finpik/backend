package finpik.profile.service.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.entity.Profile;
import lombok.Builder;

//@formatter:off
@Builder
public record UpdateProfileDto(
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
            .build();
    }
}
