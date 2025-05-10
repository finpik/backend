package com.loanpick.loanproduct.resolver.result;

import com.loanpick.loanproduct.entity.LoanProductDescription;

import lombok.Builder;

@Builder
public class LoanProductDescriptionResult {
    private final String interestRateGuide;
    private final String repaymentPeriod;
    private final String creditLoanGuide;
    private final String loanFeeGuide;
    private final String notesOnLoan;

    public LoanProductDescriptionResult(LoanProductDescription loanProductDescription) {
        interestRateGuide = loanProductDescription.getInterestRateGuide();
        repaymentPeriod = loanProductDescription.getRepaymentPeriod();
        creditLoanGuide = loanProductDescription.getCreditLoanGuide();
        loanFeeGuide = loanProductDescription.getLoanFeeGuide();
        notesOnLoan = loanProductDescription.getNotesOnLoan();
    }
}
