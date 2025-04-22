package com.loanpick.profile.resolver.input;

import com.loanpick.profile.entity.CardDelinquency;
import com.loanpick.profile.entity.LoanUsageStatus;

public record CreateProfileInput(String job, String workplaceName, String employmentForm, int income,
        String employmentDate, CardDelinquency cardDelinquency, LoanUsageStatus loanUsageStatus,
        int loanProductUsageCount, int totalLoanUsageAmount, String purposeOfLoan, int desiredLoanAmount,
        String creditGradeStatus) {
}
