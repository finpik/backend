package finpik.loanproduct;

import finpik.loanproduct.vo.InterestRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendedLoanProduct {
    private Long loanProductId;
    private String productName;
    private InterestRate interestRate;
    private Integer loanLimitAmount;

    public static RecommendedLoanProduct of(
        Long loanProductId, String productName, InterestRate interestRate, Integer loanLimitAmount
    ) {
        return new RecommendedLoanProduct(loanProductId, productName, interestRate, loanLimitAmount);
    }
}
