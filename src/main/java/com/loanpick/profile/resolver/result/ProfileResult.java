package com.loanpick.profile.resolver.result;

public record ProfileResult(
    String purposeOfLoan,
    String job,
    String creditGradeStatus,
    int loanProductUsageCount,
    int totalLoanUsageAmount,
    int desiredLoanAmount
) {
}
