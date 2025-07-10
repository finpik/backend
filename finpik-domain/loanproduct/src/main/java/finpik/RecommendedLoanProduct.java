package finpik;

import finpik.util.Preconditions;
import finpik.vo.InterestRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendedLoanProduct {
    private Long recommendedLoanProductId;
    private Long profileId;
    private Long loanProductId;
    private String productName;
    private InterestRate interestRate;
    private Long maxLoanLimitAmount;

    public static RecommendedLoanProduct of(
        Long profileId,
        Long loanProductId,
        String productName,
        Float maxInterestRate,
        Float minInterestRate,
        Long maxLoanLimitAmount
    ) {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        return new RecommendedLoanProduct(
            null,
            Preconditions.require(profileId, "profileId must not be null"),
            Preconditions.require(loanProductId, "loanProductId must not be null"),
            Preconditions.require(productName, "productName must not be null"),
            interestRate,
            maxLoanLimitAmount
        );
    }

    public static RecommendedLoanProduct rebuild(
        Long recommendedLoanProductId,
        Long profileId,
        Long loanProductId,
        String productName,
        Float maxInterestRate,
        Float minInterestRate,
        Long maxLoanLimitAmount
    ) {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        return new RecommendedLoanProduct(
            Preconditions.require(recommendedLoanProductId, "recommendedLoanProductId must not be null"),
            Preconditions.require(profileId, "profileId must not be null"),
            Preconditions.require(loanProductId, "loanProductId must not be null"),
            Preconditions.require(productName, "productName must not be null"),
            interestRate,
            maxLoanLimitAmount
        );
    }
}
