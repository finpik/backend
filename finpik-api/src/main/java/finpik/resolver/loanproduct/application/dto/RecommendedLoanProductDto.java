package finpik.resolver.loanproduct.application.dto;

import finpik.loanproduct.entity.RecommendedLoanProduct;
import lombok.Getter;

@Getter
public class RecommendedLoanProductDto {
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Integer loanLimitAmount;

    public RecommendedLoanProductDto(RecommendedLoanProduct recommendedLoanProduct) {
        this.loanProductId = recommendedLoanProduct.getLoanProductId();
        this.productName = recommendedLoanProduct.getProductName();
        this.minInterestRate = recommendedLoanProduct.getMinInterestRate();
        this.maxInterestRate = recommendedLoanProduct.getMaxInterestRate();
        this.loanLimitAmount = recommendedLoanProduct.getLoanLimitAmount();
    }
}
