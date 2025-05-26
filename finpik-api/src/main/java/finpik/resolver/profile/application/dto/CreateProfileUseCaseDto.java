package finpik.resolver.profile.application.dto;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.profile.service.dto.CreateProfileDto;
import finpik.user.entity.User;
import lombok.Builder;

//@formatter:off
@Builder
public record CreateProfileUseCaseDto(
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
    Long userId,
    ProfileColor profileColor
) {

    public CreateProfileDto toDomainDto(User user) {
        return CreateProfileDto.builder()
            .workplaceName(workplaceName)
            .employmentForm(employmentForm)
            .income(income)
            .occupation(occupation)
            .employmentDate(employmentDate)
            .loanProductUsageStatus(loanProductUsageStatus)
            .loanProductUsageCount(loanProductUsageCount)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .purposeOfLoan(purposeOfLoan)
            .desiredLoanAmount(desiredLoanAmount)
            .creditGradeStatus(creditGradeStatus)
            .creditScore(creditScore)
            .profileName(profileName)
            .profileColor(profileColor)
            .user(user)
            .build();
    }
}
