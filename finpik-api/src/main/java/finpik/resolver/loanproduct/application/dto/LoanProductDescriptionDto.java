package finpik.resolver.loanproduct.application.dto;

import finpik.loanproduct.LoanProductDescription;
import lombok.Getter;

@Getter
public class LoanProductDescriptionDto {
    private final String interestRateGuide;
    private final String repaymentPeriod;
    private final String creditLoanGuide;
    private final String loanFeeGuide;
    private final String notesOnLoan;

    public LoanProductDescriptionDto(LoanProductDescription dto) {
        interestRateGuide = dto.getInterestRateGuide();
        repaymentPeriod = dto.getRepaymentPeriod();
        creditLoanGuide = dto.getCreditLoanGuide();
        loanFeeGuide = dto.getLoanFeeGuide();
        notesOnLoan = dto.getNotesOnLoan();
    }
}
