package com.loanpick.loanproduct.resolver.result;

import com.loanpick.loanproduct.entity.LoanProduct;

import lombok.Builder;

/*
TODO(대출 필수 조건에 대해 어떻게 넣을지 확인 필요)
현재는 해당 부분이 제대로 되어 있지 않음
 */
@Builder
public class LoanProductResult {
    private final String productName;
    private final String bankName;
    private final Integer loanLimitAmount;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer loanPeriod;
    private final LoanProductDescriptionResult descriptionResult;

    public LoanProductResult(LoanProduct loanProduct) {
        productName = loanProduct.getProductName();
        bankName = loanProduct.getBankName();
        loanLimitAmount = loanProduct.getLoanLimitAmount();
        maxInterestRate = loanProduct.getMaxInterestRate();
        minInterestRate = loanProduct.getMinInterestRate();
        loanPeriod = loanProduct.getLoanPeriod();
        descriptionResult = new LoanProductDescriptionResult(loanProduct.getDescription());
    }
}
