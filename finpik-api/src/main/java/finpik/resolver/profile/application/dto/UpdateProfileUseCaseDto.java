package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.service.dto.UpdateProfileDto;
import lombok.Builder;

//@formatter:off
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
    public UpdateProfileDto toDomainDto() {
        return UpdateProfileDto.builder()
            .id(id)
            .occupation(occupation)
            .workplaceName(workplaceName)
            .employmentForm(employmentForm)
            .income(income)
            .employmentDate(employmentDate)
            .purposeOfLoan(purposeOfLoan)
            .desiredLoanAmount(desiredLoanAmount)
            .loanProductUsageStatus(loanProductUsageStatus)
            .loanProductUsageCount(loanProductUsageCount)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .creditGradeStatus(creditGradeStatus)
            .creditScore(creditScore)
            .profileName(profileName)
            .build();
    }
}
