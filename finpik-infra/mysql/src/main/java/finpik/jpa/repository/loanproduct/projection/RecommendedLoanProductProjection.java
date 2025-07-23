package finpik.jpa.repository.loanproduct.projection;

public record RecommendedLoanProductProjection(
    Long recommendedLoanProductId,
    Long profileId,
    String bankName,
    Long loanProductId,
    String productName,
    Float minInterestRate,
    Float maxInterestRate,
    Long maxLoanLimitAmount,
    Float similarity
) {
}
