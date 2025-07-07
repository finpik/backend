package finpik.profile.entity.policy;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.user.entity.User;

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
    EmploymentForm employmentForm,
    User user,
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
        EmploymentForm employmentForm, User user
    ) {
        return new ProfileCreationSpec(
            null, desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, null, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            profileColor, annualIncome, businessStartDate,
            employmentDate, occupation,
            employmentForm, user, null, null
        );
    }

    public static ProfileCreationSpec rebuild(
        Long id, Integer desiredLoanAmount, Integer loanProductUsageCount,
        Integer totalLoanUsageAmount, Integer creditScore,
        String profileName, Integer seq, CreditGradeStatus creditGradeStatus,
        LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan,
        ProfileColor profileColor, Integer annualIncome, LocalDate businessStartDate,
        LocalDate employmentDate, Occupation occupation,
        EmploymentForm employmentForm, User user,
        LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new ProfileCreationSpec(
            id, desiredLoanAmount, loanProductUsageCount,
            totalLoanUsageAmount, creditScore,
            profileName, seq, creditGradeStatus,
            loanProductUsageStatus, purposeOfLoan,
            profileColor, annualIncome, businessStartDate,
            employmentDate, occupation,
            employmentForm, user, createdAt, updatedAt
        );
    }
}
