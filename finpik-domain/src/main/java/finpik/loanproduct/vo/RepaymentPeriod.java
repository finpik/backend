package finpik.loanproduct.vo;

import finpik.entity.enums.RepaymentPeriodUnit;

public record RepaymentPeriod(Integer repaymentPeriod, RepaymentPeriodUnit repaymentPeriodUnit) {
    public RepaymentPeriod {
        validateLoanPeriod(repaymentPeriod);
        validateLoanPeriodUnit(repaymentPeriodUnit);
    }

    private void validateLoanPeriod(Integer loanPeriod) {
        if (loanPeriod == null || loanPeriod < 0) {
            throw new IllegalArgumentException("LoanPeriod must be greater than or equal to 0");
        }
    }

    private void validateLoanPeriodUnit(RepaymentPeriodUnit repaymentPeriodUnit) {
        if (repaymentPeriodUnit == null) {
            throw new IllegalArgumentException("LoanPeriodUnit must not be null");
        }
    }
}
