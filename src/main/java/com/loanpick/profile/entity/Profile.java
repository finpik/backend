package com.loanpick.profile.entity;

import java.time.LocalDate;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int desiredLoanAmount;

    private int loanProductUsageCount;

    private int totalLoanUsageAmount;

    private int creditScore;

    private int income;

    private String workplaceName;

    private String profileName;

    @Enumerated(EnumType.STRING)
    private EmploymentForm employmentForm;

    @Enumerated(EnumType.STRING)
    private CreditGradeStatus creditGradeStatus;

    @Enumerated(EnumType.STRING)
    private LoanProductUsageStatus loanProductUsageStatus;

    @Enumerated(EnumType.STRING)
    private PurposeOfLoan purposeOfLoan;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    private LocalDate employmentDate;

    @Builder
    public Profile(int desiredLoanAmount, int loanProductUsageCount, int totalLoanUsageAmount, int creditScore,
            CreditGradeStatus creditGradeStatus, int income, String workplaceName, EmploymentForm employmentForm,
            LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan, LocalDate employmentDate,
            String profileName, EmploymentStatus employmentStatus) {
        this.desiredLoanAmount = desiredLoanAmount;
        this.loanProductUsageCount = loanProductUsageCount;
        this.totalLoanUsageAmount = totalLoanUsageAmount;
        this.creditScore = creditScore;
        this.creditGradeStatus = creditGradeStatus;
        this.income = income;
        this.workplaceName = workplaceName;
        this.employmentForm = employmentForm;
        this.loanProductUsageStatus = loanProductUsageStatus;
        this.purposeOfLoan = purposeOfLoan;
        this.employmentDate = employmentDate;
        this.profileName = profileName;
        this.employmentStatus = employmentStatus;
    }
}
