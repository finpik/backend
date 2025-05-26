package finpik.loanproduct.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanProductDescription {
    private final Long id;
    private final String loanRequirement;
    private final String interestRateGuide;
    private final String repaymentPeriod;
    private final String creditLoanGuide;
    private final String loanFeeGuide;
    private final String notesOnLoan;
}
