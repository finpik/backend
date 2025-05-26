package finpik.loanproduct.entity;

import lombok.Builder;
import lombok.Getter;

//@formatter:off
@Getter
@Builder
public class RecommendedLoanProduct {
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Integer loanLimitAmount;
}
