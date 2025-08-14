package finpik.resolver.profile.resolver.input;

import java.time.LocalDate;

import finpik.entity.enums.BusinessType;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.PurposeOfLoan;
import finpik.resolver.profile.application.dto.UpdateProfileUseCaseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateProfileInput(
    @NotNull(message = "프로필 id가 필요합니다.")
    Long profileId,
    Occupation occupation,
    EmploymentForm employmentForm,
    Integer annualIncome,
    LocalDate employmentDate,
    PurposeOfLoan purposeOfLoan,
    @Min(value = 0, message = "대출 희망 금액은 0원 이상 작성해주세요.")
    Integer desiredLoanAmount,
    LoanProductUsageStatus loanProductUsageStatus,
    @Min(value = 0, message = "대출 개수는 0개 이상 작성해주세요.")
    Integer loanProductUsageCount,
    @Min(value = 0, message = "대출 금액은 0원 이상 작성해주세요.")
    Integer totalLoanUsageAmount,
    Integer creditScore,
    CreditGradeStatus creditGradeStatus,
    @Size(min = 1, max = 14, message = "프로필 이름은 1자 이상 14자 이하로 작성해주세요.")
    String profileName,
    LocalDate businessStartDate,
    BusinessType businessType
) {
    public UpdateProfileUseCaseDto toDto() {
        return UpdateProfileUseCaseDto.builder()
            .profileId(profileId)
            .occupation(occupation)
            .employmentForm(employmentForm)
            .annualIncome(annualIncome)
            .employmentDate(employmentDate)
            .purposeOfLoan(purposeOfLoan)
            .desiredLoanAmount(desiredLoanAmount)
            .loanProductUsageStatus(loanProductUsageStatus)
            .loanProductUsageCount(loanProductUsageCount)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .creditGradeStatus(creditGradeStatus)
            .creditScore(creditScore)
            .profileName(profileName)
            .businessStartDate(businessStartDate)
            .businessType(businessType)
            .build();
    }
}
