package finpik.service.loanproduct.dto;

import finpik.RecommendedLoanProduct;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CachedRecommendedLoanProduct(
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

    public RecommendedLoanProduct toDomain() {
        return RecommendedLoanProduct.rebuild(
            recommendedLoanProductId,
            profileId,
            bankName,
            loanProductId,
            productName,
            maxInterestRate,
            minInterestRate,
            maxLoanLimitAmount,
            similarity
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
            .similarity(recommendedLoanProduct.getSimilarity())
            .build();
    }
}
