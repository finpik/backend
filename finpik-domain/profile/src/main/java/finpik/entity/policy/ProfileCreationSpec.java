package finpik.entity.policy;

import finpik.entity.User;
import finpik.entity.enums.BusinessType;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileCreationSpec(
    Long id,
    Integer desiredLoanAmount,
    Integer loanProductUsageCount,
    Integer totalLoanUsageAmount,
    Integer creditScore,
    String profileName,
    Integer seq,
    CreditGradeStatus creditGradeStatus,
    LoanProductUsageStatus loanProductUsageStatus,
    PurposeOfLoan purposeOfLoan,
    ProfileColor profileColor,
    Integer annualIncome,
    LocalDate businessStartDate,
    LocalDate employmentDate,
    Occupation occupation,
    BusinessType businessType,
    EmploymentForm employmentForm,
    User user,
    Integer recommendedLoanProductCount,
    Double minInterestRate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ProfileCreationSpec createNew(
        Integer desiredLoanAmount, Integer loanProductUsageCount,
        Integer totalLoanUsageAmount, Integer creditScore,
        String profileName, CreditGradeStatus creditGradeStatus,
        LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan,
        ProfileColor profileColor, Integer annualIncome, LocalDate businessStartDate,
        LocalDate employmentDate, Occupation occupation,
        EmploymentForm employmentForm, User user, BusinessType businessType
    ) {
        Integer newRecommendedLoanProductCount = 0;
        Double newMinInterestRate = 0.0;

        return new ProfileCreationSpec(
            null, desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, null, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            profileColor, annualIncome, businessStartDate,
            employmentDate, occupation, businessType,
            employmentForm, user, newRecommendedLoanProductCount,
            newMinInterestRate, null, null
        );
    }

    public static ProfileCreationSpec rebuild(
        Long id, Integer desiredLoanAmount, Integer loanProductUsageCount,
        Integer totalLoanUsageAmount, Integer creditScore,
        String profileName, Integer seq, CreditGradeStatus creditGradeStatus,
        LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan,
        ProfileColor profileColor, Integer annualIncome, LocalDate businessStartDate,
        LocalDate employmentDate, Occupation occupation, BusinessType businessType,
        EmploymentForm employmentForm, User user, Integer recommendedLoanProductCount,
        Double minInterestRate, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new ProfileCreationSpec(
            id, desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, seq, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            profileColor, annualIncome, businessStartDate,
            employmentDate, occupation, businessType,
            employmentForm, user, recommendedLoanProductCount,
            minInterestRate, createdAt, updatedAt
        );
    }
}
