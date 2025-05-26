package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.LoanProductDescriptionDto;
import lombok.Getter;

@Getter
public class LoanProductDescriptionResult {
    private final String interestRateGuide;
    private final String repaymentPeriod;
    private final String creditLoanGuide;
    private final String loanFeeGuide;
    private final String notesOnLoan;

    public LoanProductDescriptionResult(LoanProductDescriptionDto dto) {
        interestRateGuide = dto.getInterestRateGuide();
        repaymentPeriod = dto.getRepaymentPeriod();
        creditLoanGuide = dto.getCreditLoanGuide();
        loanFeeGuide = dto.getLoanFeeGuide();
        notesOnLoan = dto.getNotesOnLoan();
    }
}
