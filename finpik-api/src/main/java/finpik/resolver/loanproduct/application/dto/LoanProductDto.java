package finpik.resolver.loanproduct.application.dto;

import finpik.LoanProduct;
import finpik.entity.enums.RepaymentPeriodUnit;
import lombok.Getter;

@Getter
public class LoanProductDto {
    private final Long loanProductId;
    private final String productName;
    private final String bankName;
    private final Long maxLoanLimitAmount;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer repaymentPeriod;
    private final RepaymentPeriodUnit repaymentPeriodUnit;
    private final LoanProductDescriptionDto description;
    private final String url;

    public LoanProductDto(LoanProduct loanProduct) {
        loanProductId = loanProduct.getId();
        productName = loanProduct.getProductName();
        bankName = loanProduct.getBankDetails().bankName();
        maxLoanLimitAmount = loanProduct.getMaxLoanLimitAmount();
        maxInterestRate = loanProduct.getInterestRate().maxInterestRate();
        minInterestRate = loanProduct.getInterestRate().minInterestRate();
        repaymentPeriod = loanProduct.getRepaymentPeriod().repaymentPeriod();
        repaymentPeriodUnit = loanProduct.getRepaymentPeriod().repaymentPeriodUnit();
        description = new LoanProductDescriptionDto(loanProduct.getDescription());
        url = loanProduct.getUrl();
    }
}
