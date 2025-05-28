package finpik.loanproduct.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RelatedLoanProduct {
    private final Long productId;
    private final String productName;
    private final Float maxInterestRate;
    private final Float minInterestRate;
    private final Integer loanLimitAmount;
}
