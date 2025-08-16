package finpik.dto;

import finpik.entity.enums.CertificateRequirement;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.LoanProductBadge;
import finpik.entity.enums.RepaymentPeriodUnit;
import finpik.entity.enums.Occupation;

import java.util.List;

public record LoanProductCreationDto(
    Long id,
    String productName,
    Integer repaymentPeriod,
    String bankName,
    String bankPhoneNumber,
    String loanAvailableTime,
    Float maxInterestRate,
    Float minInterestRate,
    RepaymentPeriodUnit repaymentPeriodUnit,
    Integer minAge,
    Integer maxAge,
    Gender genderLimit,
    Long maxLoanLimitAmount,
    String loanPrerequisite,
    String loanTargetGuide,
    String certificationRequirementGuide,
    String loanLimitGuide,
    String repaymentPeriodGuide,
    String interestRateGuide,
    String loanAvailableTimeGuide,
    String repaymentFeeGuide,
    String delinquencyInterestRateGuide,
    String notesOnLoan,
    String preLoanChecklist,
    CertificateRequirement certificateRequirement,
    Occupation occupation,
    String url,
    Integer minCreditScore,
    EmploymentForm employmentForm,
    String bankImgUrl,
    List<LoanProductBadge> loanProductBadgeList
) {
}
