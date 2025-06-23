package finpik.loanproduct;

import finpik.loanproduct.vo.InterestRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelatedLoanProduct {
    private Long productId;
    private String productName;
    private InterestRate interestRate;
    private Long maxLoanLimitAmount;

    public static RelatedLoanProduct of(
        Long productId,
        String productName,
        InterestRate interestRate,
        Long maxLoanLimitAmount
    ) {
        return new RelatedLoanProduct(productId, productName, interestRate, maxLoanLimitAmount);
    }
}
