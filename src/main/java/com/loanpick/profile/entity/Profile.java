package com.loanpick.profile.entity;

import java.time.LocalDate;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.Occupation;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.ProfileColor;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "userid_seq_uk", columnNames = {"user_id", "seq"})})
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer desiredLoanAmount;

    private Integer loanProductUsageCount;

    private Integer totalLoanUsageAmount;

    private Integer creditScore;

    private Integer income;

    private String workplaceName;

    private String profileName;

    @Description("프로필의 보여지는 순번")
    private Integer seq;

    @Enumerated(EnumType.STRING)
    private EmploymentForm employmentForm;

    @Enumerated(EnumType.STRING)
    private CreditGradeStatus creditGradeStatus;

    @Enumerated(EnumType.STRING)
    private LoanProductUsageStatus loanProductUsageStatus;

    @Enumerated(EnumType.STRING)
    private PurposeOfLoan purposeOfLoan;

    @Enumerated(EnumType.STRING)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    private ProfileColor profileColor;

    private LocalDate employmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //@formatter:off
    @Builder
    public Profile(
        Integer desiredLoanAmount, Integer loanProductUsageCount, Integer totalLoanUsageAmount,
        Integer creditScore, CreditGradeStatus creditGradeStatus, Integer income, Integer seq,
        String workplaceName, EmploymentForm employmentForm, LoanProductUsageStatus loanProductUsageStatus,
        PurposeOfLoan purposeOfLoan, LocalDate employmentDate, String profileName,
        Occupation occupation, User user, Long id, ProfileColor profileColor
    ) {
        validateInfoRelatedEmploymentStatus(occupation, income, workplaceName, employmentDate);

        this.id = id;
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
        this.occupation = occupation;
        this.user = user;
        this.profileColor = profileColor;
        this.seq = seq == null ? 0 : seq;
    }

    public void updateProfile(Profile profile) {
        updateInfoRelatedEmploymentStatus(
            profile.getOccupation(), profile.getIncome(),
            profile.getWorkplaceName(), profile.getEmploymentDate()
        );

        this.desiredLoanAmount = profile.getDesiredLoanAmount();
        this.loanProductUsageCount = profile.getLoanProductUsageCount();
        this.totalLoanUsageAmount = profile.getTotalLoanUsageAmount();
        this.creditScore = profile.getCreditScore();
        this.creditGradeStatus = profile.getCreditGradeStatus();
        this.employmentForm = profile.getEmploymentForm();
        this.loanProductUsageStatus = profile.getLoanProductUsageStatus();
        this.purposeOfLoan = profile.getPurposeOfLoan();
        this.profileName = profile.getProfileName();
    }

    public void updateSequence(Integer seq) {
        this.seq = seq;
    }

    private void validateInfoRelatedEmploymentStatus(
        Occupation occupation,
        Integer income,
        String workplaceName,
        LocalDate employmentDate
    ) {
        if (occupation == Occupation.EMPLOYEE) {
            if (income == null || workplaceName == null || employmentDate == null) {
                throw new BusinessException(ErrorCode.INVALID_EMPLOYMENT_INFO);
            }
        } else {
            if (income != null || workplaceName != null || employmentDate != null) {
                throw new BusinessException(ErrorCode.INVALID_NOT_EMPLOYMENT_INFO);
            }
        }
    }

    public void updateInfoRelatedEmploymentStatus(
        Occupation occupation,
        Integer income,
        String workplaceName,
        LocalDate employmentDate
    ) {
        if (occupation == Occupation.EMPLOYEE) {
            if (income == null || workplaceName == null || employmentForm == null || employmentDate == null) {
                throw new BusinessException(ErrorCode.INVALID_EMPLOYMENT_INFO);
            } else {
                this.occupation = occupation;
                this.income = income;
                this.workplaceName = workplaceName;
                this.employmentDate = employmentDate;
            }
        } else {
            this.occupation = occupation;
            this.income = null;
            this.workplaceName = null;
            this.employmentDate = null;
        }
    }

    public void changeProfileColor(ProfileColor profileColor) {
        this.profileColor = profileColor;
    }
}
