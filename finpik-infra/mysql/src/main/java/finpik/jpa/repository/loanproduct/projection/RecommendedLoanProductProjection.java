package finpik.jpa.repository.loanproduct.projection;

public record RecommendedLoanProductProjection(
    Long recommendedLoanProductId,
    Long profileId,
    Long loanProductId,
    String productName,
    Float minInterestRate,
    Float maxInterestRate,
    Integer maxLoanLimitAmount
) {
}
