package finpik;

import finpik.vo.InterestRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelatedLoanProduct {
    private Long loanProductId;
    private String productName;
    private InterestRate interestRate;
    private Long maxLoanLimitAmount;

    public static RelatedLoanProduct of(
        Long loanProductId,
        String productName,
        Float maxInterestRate,
        Float minInterestRate,
        Long maxLoanLimitAmount
    ) {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        return new RelatedLoanProduct(loanProductId, productName, interestRate, maxLoanLimitAmount);
    }
}
