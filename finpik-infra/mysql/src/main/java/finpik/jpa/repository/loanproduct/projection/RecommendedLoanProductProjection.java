package finpik.jpa.repository.loanproduct.projection;

import java.util.UUID;

public record RecommendedLoanProductProjection(
    UUID recommendedLoanProductId,
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
