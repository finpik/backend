package finpik.resolver.loanproduct.application.dto;

import finpik.entity.enums.LoanPeriodUnit;
import finpik.loanproduct.LoanProduct;
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
    private final LoanPeriodUnit loanPeriodUnit;
    private final LoanProductDescriptionDto description;

    public LoanProductDto(LoanProduct loanProduct) {
        loanProductId = loanProduct.getId();
        productName = loanProduct.getProductName();
        bankName = loanProduct.getBankName();
        loanLimitAmount = loanProduct.getLoanLimitAmount();
        maxInterestRate = loanProduct.getInterestRate().maxInterestRate();
        minInterestRate = loanProduct.getInterestRate().minInterestRate();
        loanPeriod = loanProduct.getLoanPeriod().loanPeriod();
        loanPeriodUnit = loanProduct.getLoanPeriod().loanPeriodUnit();
        description = new LoanProductDescriptionDto(loanProduct.getDescription());
    }
}
