package finpik.resolver.loanproduct.application.dto;

import finpik.RelatedLoanProduct;
import lombok.Getter;

@Getter
public class RelatedLoanProductDto {
    private final Long loanProductId;
    private final String productName;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Long maxLoanLimitAmount;

    public RelatedLoanProductDto(RelatedLoanProduct relatedLoanProduct) {
        loanProductId = relatedLoanProduct.getLoanProductId();
        productName = relatedLoanProduct.getProductName();
        maxInterestRate = relatedLoanProduct.getInterestRate().minInterestRate();
        minInterestRate = relatedLoanProduct.getInterestRate().maxInterestRate();
        maxLoanLimitAmount = relatedLoanProduct.getMaxLoanLimitAmount();
    }
}
