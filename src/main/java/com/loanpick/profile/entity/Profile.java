package com.loanpick.profile.entity;

import java.time.LocalDate;

import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jdk.jfr.Description;
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

    @Description("프로필의 보여지는 순번")
    private int seq;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Profile(int desiredLoanAmount, int loanProductUsageCount, int totalLoanUsageAmount, int creditScore,
            CreditGradeStatus creditGradeStatus, int income, String workplaceName, EmploymentForm employmentForm,
            LoanProductUsageStatus loanProductUsageStatus, PurposeOfLoan purposeOfLoan, LocalDate employmentDate,
            String profileName, EmploymentStatus employmentStatus, User user, int seq) {
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
        this.user = user;
        this.seq = seq;
    }

    public void balanceSequence() {
        seq++;
    }
    public void updateProfile(Profile profile) {
        this.desiredLoanAmount = profile.getDesiredLoanAmount();
        this.loanProductUsageCount = profile.getLoanProductUsageCount();
        this.totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        this.creditScore = profile.getCreditScore();
        this.creditGradeStatus = profile.getCreditGradeStatus();
        this.income = profile.getIncome();
        this.workplaceName = profile.getWorkplaceName();
        this.employmentForm = profile.getEmploymentForm();
        this.loanProductUsageStatus = profile.getLoanProductUsageStatus();
        this.purposeOfLoan = profile.getPurposeOfLoan();
        this.employmentDate = profile.getEmploymentDate();
        this.profileName = profile.getProfileName();
        this.employmentStatus = profile.getEmploymentStatus();
    }
}
