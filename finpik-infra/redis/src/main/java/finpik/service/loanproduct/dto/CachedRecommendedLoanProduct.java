package finpik.service.loanproduct.dto;

import finpik.loanproduct.RecommendedLoanProduct;
import finpik.loanproduct.vo.InterestRate;
import lombok.Builder;

@Builder
public record CachedRecommendedLoanProduct(
    Long loanProductId,
    String productName,
    Float minInterestRate,
    Float maxInterestRate,
    Integer loanLimitAmount
) {

    public RecommendedLoanProduct toDomain() {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        return RecommendedLoanProduct.of(
                loanProductId, productName, interestRate, loanLimitAmount
            );
    }

    public static CachedRecommendedLoanProduct from(RecommendedLoanProduct recommendedLoanProduct) {
        return CachedRecommendedLoanProduct.builder()
            .loanProductId(recommendedLoanProduct.getLoanProductId())
            .productName(recommendedLoanProduct.getProductName())
            .minInterestRate(recommendedLoanProduct.getInterestRate().minInterestRate())
            .maxInterestRate(recommendedLoanProduct.getInterestRate().maxInterestRate())
            .loanLimitAmount(recommendedLoanProduct.getLoanLimitAmount())
            .build();
    }
}
