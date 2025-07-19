package finpik.service.loanproduct.dto;

import finpik.RecommendedLoanProduct;
import lombok.Builder;

@Builder
public record CachedRecommendedLoanProduct(
    Long recommendedLoanProductId,
    Long profileId,
    String bankName,
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
            bankName,
            loanProductId,
            productName,
            maxInterestRate,
            minInterestRate,
            maxLoanLimitAmount
        );
    }

    public static CachedRecommendedLoanProduct from(RecommendedLoanProduct recommendedLoanProduct) {
        return CachedRecommendedLoanProduct.builder()
            .recommendedLoanProductId(recommendedLoanProduct.getRecommendedLoanProductId())
            .profileId(recommendedLoanProduct.getProfileId())
            .loanProductId(recommendedLoanProduct.getLoanProductId())
            .bankName(recommendedLoanProduct.getBankName())
            .productName(recommendedLoanProduct.getProductName())
            .minInterestRate(recommendedLoanProduct.getInterestRate().minInterestRate())
            .maxInterestRate(recommendedLoanProduct.getInterestRate().maxInterestRate())
            .maxLoanLimitAmount(recommendedLoanProduct.getMaxLoanLimitAmount())
            .build();
    }
}
