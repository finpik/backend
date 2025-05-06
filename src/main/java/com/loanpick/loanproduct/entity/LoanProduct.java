package com.loanpick.loanproduct.entity;

import com.loanpick.loanproduct.entity.enums.LoanPeriodUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 저장은 대부분의 경우 Excel 저장으로
 */
@Entity
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private float maxInterestRate;

    private float minInterestRate;

    private int maxCreditLine;

    private int loanPeriod;

    @Enumerated(EnumType.STRING)
    private LoanPeriodUnit loanPeriodUnit;

    private String loanRequirement;

    private String interestRateGuide;

    private String repaymentPeriod;

    private String creditLoanGuide;

    private String loanFeeGuide;

    private String notesOnLoan;
}
