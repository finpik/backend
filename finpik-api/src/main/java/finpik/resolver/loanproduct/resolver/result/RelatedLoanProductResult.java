package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import lombok.Getter;

@Getter
public class RelatedLoanProductResult {
    private final Long loanProductId;
    private final String productName;
    private final String bankName;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Long maxLoanLimitAmount;

    public RelatedLoanProductResult(RelatedLoanProductDto dto) {
        loanProductId = dto.getLoanProductId();
        productName = dto.getProductName();
        bankName = dto.getBankName();
        maxInterestRate = dto.getMaxInterestRate();
        minInterestRate = dto.getMinInterestRate();
        maxLoanLimitAmount = dto.getMaxLoanLimitAmount();
    }
}
