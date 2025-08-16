package finpik;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
