package finpik.resolver.loanproduct.application.dto;

import finpik.RecommendedLoanProduct;
import lombok.Getter;

@Getter
public class RecommendedLoanProductDto {
    private final Long recommendedLoanProductId;
    private final Long profileId;
    private final String bankName;
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Long maxLoanLimitAmount;

    public RecommendedLoanProductDto(RecommendedLoanProduct recommendedLoanProduct) {
        this.recommendedLoanProductId = recommendedLoanProduct.getRecommendedLoanProductId();
        this.profileId = recommendedLoanProduct.getProfileId();
        this.bankName = recommendedLoanProduct.getBankName();
        this.loanProductId = recommendedLoanProduct.getLoanProductId();
        this.productName = recommendedLoanProduct.getProductName();
        this.minInterestRate = recommendedLoanProduct.getInterestRate().minInterestRate();
        this.maxInterestRate = recommendedLoanProduct.getInterestRate().maxInterestRate();
        this.maxLoanLimitAmount = recommendedLoanProduct.getMaxLoanLimitAmount();
    }
}
