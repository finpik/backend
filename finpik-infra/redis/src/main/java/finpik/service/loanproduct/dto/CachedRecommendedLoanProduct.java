package finpik.service.loanproduct.dto;

import finpik.loanproduct.RecommendedLoanProduct;
import lombok.Builder;

@Builder
public record CachedRecommendedLoanProduct(
    Long recommendedLoanProductId,
    Long profileId,
    Long loanProductId,
    String productName,
    Float minInterestRate,
    Float maxInterestRate,
    Long maxLoanLimitAmount
) {

    public RecommendedLoanProduct toDomain() {
        return RecommendedLoanProduct.rebuild(
            recommendedLoanProductId,
            profileId,
            loanProductId,
            productName,
            maxInterestRate,
            minInterestRate,
            maxLoanLimitAmount
        );
    }

    public static CachedRecommendedLoanProduct from(RecommendedLoanProduct recommendedLoanProduct) {
        return CachedRecommendedLoanProduct.builder()
            .loanProductId(recommendedLoanProduct.getLoanProductId())
            .productName(recommendedLoanProduct.getProductName())
            .minInterestRate(recommendedLoanProduct.getInterestRate().minInterestRate())
            .maxInterestRate(recommendedLoanProduct.getInterestRate().maxInterestRate())
            .maxLoanLimitAmount(recommendedLoanProduct.getMaxLoanLimitAmount())
            .build();
    }
}
