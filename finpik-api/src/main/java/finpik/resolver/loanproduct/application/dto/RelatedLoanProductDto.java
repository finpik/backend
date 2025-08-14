package finpik.resolver.loanproduct.application.dto;

import finpik.RelatedLoanProduct;
import lombok.Getter;

@Getter
public class RelatedLoanProductDto {
    private final Long loanProductId;
    private final String productName;
    private final String bankName;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Long maxLoanLimitAmount;

    public RelatedLoanProductDto(RelatedLoanProduct relatedLoanProduct) {
        loanProductId = relatedLoanProduct.getLoanProductId();
        productName = relatedLoanProduct.getProductName();
        bankName = relatedLoanProduct.getBankName();
        maxInterestRate = relatedLoanProduct.getInterestRate().maxInterestRate();
        minInterestRate = relatedLoanProduct.getInterestRate().minInterestRate();
        maxLoanLimitAmount = relatedLoanProduct.getMaxLoanLimitAmount();
    }
}
