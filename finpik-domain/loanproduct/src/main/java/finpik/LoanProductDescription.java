package finpik;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanProductDescription {
    private final String loanPrerequisite;
    private final String loanTargetGuide;
    private final String maxLoanLimitGuide;
    private final String repaymentPeriodGuide;
    private final String interestRateGuide;
    private final String certificationRequirementGuide;
    private final String loanAvailableTimeGuide;
    private final String repaymentFeeGuide;
    private final String delinquencyInterestRateGuide;
    private final String notesOnLoan;
    private final String preLoanChecklist;
}
