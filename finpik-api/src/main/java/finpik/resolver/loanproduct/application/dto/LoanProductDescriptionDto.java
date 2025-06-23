package finpik.resolver.loanproduct.application.dto;

import finpik.loanproduct.LoanProductDescription;
import lombok.Getter;

@Getter
public class LoanProductDescriptionDto {
    private final String loanPrerequisite;
    private final String loanTargetGuide;
    private final String interestRateGuide;
    private final String maxLoanLimitGuide;
    private final String repaymentPeriodGuide;
    private final String notesOnLoan;
    private final String preLoanChecklist;

    public LoanProductDescriptionDto(LoanProductDescription description) {
        loanPrerequisite = description.getLoanPrerequisite();
        loanTargetGuide = description.getLoanTargetGuide();
        interestRateGuide = description.getInterestRateGuide();
        maxLoanLimitGuide = description.getMaxLoanLimitGuide();
        repaymentPeriodGuide = description.getRepaymentPeriodGuide();
        notesOnLoan = description.getNotesOnLoan();
        preLoanChecklist = description.getPreLoanChecklist();
    }
}
