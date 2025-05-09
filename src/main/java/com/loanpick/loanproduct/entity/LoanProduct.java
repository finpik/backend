package com.loanpick.loanproduct.entity;

import com.loanpick.common.entity.enums.Occupation;
import com.loanpick.loanproduct.entity.enums.LoanPeriodUnit;
import com.loanpick.profile.entity.enums.PurposeOfLoan;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 저장은 대부분의 경우 Excel 저장으로
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Float maxInterestRate;

    private Float minInterestRate;

    private Integer maxCreditLine;

    private Integer loanPeriod;

    private Integer maxCreditGrade;

    private Integer minCreditGrade;

    private Integer age;

    private Integer loanLimitAmount;

    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_description_id")
    private LoanProductDescription description;

    @Enumerated(EnumType.STRING)
    private LoanPeriodUnit loanPeriodUnit;

    @Enumerated(EnumType.STRING)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    private PurposeOfLoan purposeOfLoan;
}
