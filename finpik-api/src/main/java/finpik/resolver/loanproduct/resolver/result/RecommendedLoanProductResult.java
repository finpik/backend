package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import lombok.Getter;

//@formatter:off
@Getter
public class RecommendedLoanProductResult {
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Integer loanLimitAmount;

    public RecommendedLoanProductResult (RecommendedLoanProductDto dto) {
        loanProductId = dto.getLoanProductId();
        productName = dto.getProductName();
        minInterestRate = dto.getMinInterestRate();
        maxInterestRate = dto.getMaxInterestRate();
        loanLimitAmount = dto.getLoanLimitAmount();
    }
}
