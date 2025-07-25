package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import lombok.Getter;

@Getter
public class RecommendedLoanProductResult {
    private final Long recommendedLoanProductId;
    private final Long profileId;
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Long maxLoanLimitAmount;

    public RecommendedLoanProductResult (RecommendedLoanProductDto dto) {
        recommendedLoanProductId = dto.getRecommendedLoanProductId();
        profileId = dto.getProfileId();
        loanProductId = dto.getLoanProductId();
        productName = dto.getProductName();
        minInterestRate = dto.getMinInterestRate();
        maxInterestRate = dto.getMaxInterestRate();
        maxLoanLimitAmount = dto.getMaxLoanLimitAmount();
    }

    public RecommendedLoanProductResult(
        Long recommendedLoanProductId, Long profileId,
        Long loanProductId, String productName, Float minInterestRate,
        Float maxInterestRate, Long loanLimitAmount
    ) {
        this.recommendedLoanProductId = recommendedLoanProductId;
        this.profileId = profileId;
        this.loanProductId = loanProductId;
        this.productName = productName;
        this.minInterestRate = minInterestRate;
        this.maxInterestRate = maxInterestRate;
        this.maxLoanLimitAmount = loanLimitAmount;
    }
}
