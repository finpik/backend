package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.LoanProductDescriptionDto;
import lombok.Getter;

@Getter
public class LoanProductDescriptionResult {
    private final String loanPrerequisite;
    private final String loanTargetGuide;
    private final String interestRateGuide;
    private final String maxLoanLimitGuide;
    private final String repaymentPeriodGuide;
    private final String notesOnLoan;
    private final String preLoanChecklist;

    public LoanProductDescriptionResult(LoanProductDescriptionDto dto) {
        loanPrerequisite = dto.getLoanPrerequisite();
        loanTargetGuide = dto.getLoanTargetGuide();
        interestRateGuide = dto.getInterestRateGuide();
        maxLoanLimitGuide = dto.getMaxLoanLimitGuide();
        repaymentPeriodGuide = dto.getRepaymentPeriodGuide();
        notesOnLoan = dto.getNotesOnLoan();
        preLoanChecklist = dto.getPreLoanChecklist();
    }
}
