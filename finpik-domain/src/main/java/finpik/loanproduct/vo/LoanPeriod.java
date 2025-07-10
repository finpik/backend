package finpik.loanproduct.vo;

import finpik.entity.enums.LoanPeriodUnit;

public record LoanPeriod(Integer loanPeriod, LoanPeriodUnit loanPeriodUnit) {
    public LoanPeriod {
        validateLoanPeriod(loanPeriod);
        validateLoanPeriodUnit(loanPeriodUnit);
    }

    private void validateLoanPeriod(Integer loanPeriod) {
        if (loanPeriod == null || loanPeriod < 0) {
            throw new IllegalArgumentException("LoanPeriod must be greater than or equal to 0");
        }
    }

    private void validateLoanPeriodUnit(LoanPeriodUnit loanPeriodUnit) {
        if (loanPeriodUnit == null) {
            throw new IllegalArgumentException("LoanPeriodUnit must not be null");
        }
    }
}
