package finpik.vo;

import finpik.entity.enums.RepaymentPeriodUnit;

public record RepaymentPeriod(Integer repaymentPeriod, RepaymentPeriodUnit repaymentPeriodUnit) {
    public RepaymentPeriod {
        validateLoanPeriod(repaymentPeriod);
        validateLoanPeriodUnit(repaymentPeriodUnit);
    }

    private void validateLoanPeriod(Integer loanPeriod) {
        if (loanPeriod == null) {
            throw new IllegalArgumentException("LoanPeriod must not be null");
        }

        if (loanPeriod < 0) {
            throw new IllegalArgumentException("LoanPeriod must not be negative");
        }
    }

    private void validateLoanPeriodUnit(RepaymentPeriodUnit repaymentPeriodUnit) {
        if (repaymentPeriodUnit == null) {
            throw new IllegalArgumentException("RepaymentPeriodUnit must not be null");
        }
    }
}
