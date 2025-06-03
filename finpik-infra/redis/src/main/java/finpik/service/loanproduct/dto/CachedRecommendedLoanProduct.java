package finpik.service.loanproduct.dto;

import finpik.loanproduct.entity.RecommendedLoanProduct;
import lombok.Builder;

//@formatter:off
@Builder
public record CachedRecommendedLoanProduct(
    Long loanProductId,
    String productName,
    Float minInterestRate,
    Float maxInterestRate,
    Integer loanLimitAmount
) {

    public RecommendedLoanProduct toDomain() {
        return RecommendedLoanProduct.builder()
            .loanProductId(loanProductId)
            .productName(productName)
            .minInterestRate(minInterestRate)
            .maxInterestRate(maxInterestRate)
            .loanLimitAmount(loanLimitAmount)
            .build();
    }

    public static CachedRecommendedLoanProduct from(RecommendedLoanProduct recommendedLoanProduct) {
        return CachedRecommendedLoanProduct.builder()
            .loanProductId(recommendedLoanProduct.getLoanProductId())
            .productName(recommendedLoanProduct.getProductName())
            .minInterestRate(recommendedLoanProduct.getMinInterestRate())
            .maxInterestRate(recommendedLoanProduct.getMaxInterestRate())
            .loanLimitAmount(recommendedLoanProduct.getLoanLimitAmount())
            .build();
    }
}
