package finpik.resolver.loanproduct.application.dto;

import finpik.loanproduct.entity.LoanProduct;
import lombok.Getter;

@Getter
public class LoanProductDto {
    private final Long loanProductId;
    private final String productName;
    private final String bankName;
    private final Integer loanLimitAmount;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer loanPeriod;
    private final LoanProductDescriptionDto description;

    public LoanProductDto(LoanProduct loanProduct) {
        loanProductId = loanProduct.getId();
        productName = loanProduct.getProductName();
        bankName = loanProduct.getBankName();
        loanLimitAmount = loanProduct.getLoanLimitAmount();
        maxInterestRate = loanProduct.getMaxInterestRate();
        minInterestRate = loanProduct.getMinInterestRate();
        loanPeriod = loanProduct.getLoanPeriod();
        description = new LoanProductDescriptionDto(loanProduct.getDescription());
    }
}
