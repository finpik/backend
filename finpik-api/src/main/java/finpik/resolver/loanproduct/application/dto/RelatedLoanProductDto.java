package finpik.resolver.loanproduct.application.dto;

import finpik.loanproduct.entity.RelatedLoanProduct;
import lombok.Getter;

@Getter
public class RelatedLoanProductDto {
    private final Long productId;
    private final String productName;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer loanLimitAmount;

    public RelatedLoanProductDto(RelatedLoanProduct relatedLoanProduct) {
        productId = relatedLoanProduct.getProductId();
        productName = relatedLoanProduct.getProductName();
        maxInterestRate = relatedLoanProduct.getMaxInterestRate();
        minInterestRate = relatedLoanProduct.getMinInterestRate();
        loanLimitAmount = relatedLoanProduct.getLoanLimitAmount();
    }
}
