package finpik.resolver.profile.resolver.input;

import java.time.LocalDate;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.resolver.profile.application.dto.CreateProfileUseCaseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateProfileInput(
    @NotNull(message = "직업을 선택해 주세요.")
    Occupation occupation,
    EmploymentForm employmentForm,
    Integer annualIncome,
    LocalDate employmentDate,
    @NotNull(message = "대출 목적을 선택해 주세요.")
    PurposeOfLoan purposeOfLoan,
    @NotNull(message = "대출 희망 금액을 적어주세요.")
    @Min(value = 0, message = "대출 희망 금액은 0원 이상 작성해주세요.")
    Integer desiredLoanAmount,
    @NotNull(message = "이용하는 서비스가 있는지 선택해주세요.")
    LoanProductUsageStatus loanProductUsageStatus,
    @NotNull(message = "현재 가입하신 대출 개수를 적어주세요.")
    @Min(value = 0, message = "대출 개수는 0개 이상 작성해주세요.")
    Integer loanProductUsageCount,
    @NotNull(message = "현재 가입하신 모든 대출 금액을 적어주세요.")
    @Min(value = 0, message = "대출 금액은 0원 이상 작성해주세요.")
    Integer totalLoanUsageAmount,
    Integer creditScore,
    CreditGradeStatus creditGradeStatus,
    @NotBlank(message = "작성하신 프로필의 이름을 만들어주세요.")
    @Size(min = 1, max = 14, message = "프로필 이름은 1자 이상 14자 이하로 작성해주세요.")
    String profileName,
    ProfileColor profileColor
) {
    public CreateProfileUseCaseDto toDto(Long userId) {
        return CreateProfileUseCaseDto.builder()
            .employmentForm(employmentForm)
            .annualIncome(annualIncome)
            .employmentDate(employmentDate)
            .loanProductUsageStatus(loanProductUsageStatus)
            .loanProductUsageCount(loanProductUsageCount)
            .totalLoanUsageAmount(totalLoanUsageAmount)
            .purposeOfLoan(purposeOfLoan)
            .desiredLoanAmount(desiredLoanAmount)
            .creditGradeStatus(creditGradeStatus)
            .creditScore(creditScore)
            .profileName(profileName)
            .occupation(occupation)
            .profileColor(profileColor)
            .userId(userId)
            .build();
    }
}
