package finpik;

import finpik.entity.enums.LoanProductBadge;
import finpik.util.Preconditions;
import finpik.vo.InterestRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendedLoanProduct {
    private UUID recommendedLoanProductId;
    private Long profileId;
    private String bankName;
    private Long loanProductId;
    private String productName;
    private InterestRate interestRate;
    private Long maxLoanLimitAmount;
    private Float similarity;
    private List<LoanProductBadge> loanProductBadges;

    public static RecommendedLoanProduct of(
        Long profileId,
        Long loanProductId,
        String bankName,
        String productName,
        Float maxInterestRate,
        Float minInterestRate,
        Long maxLoanLimitAmount,
        Float similarity
    ) {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);
        List<LoanProductBadge> loanProductBadges = new ArrayList<>();

        return new RecommendedLoanProduct(
            null,
            Preconditions.require(profileId, "profileId must not be null"),
            Preconditions.require(bankName, "bankName must not be null"),
            Preconditions.require(loanProductId, "loanProductId must not be null"),
            Preconditions.require(productName, "productName must not be null"),
            interestRate,
            maxLoanLimitAmount,
            Preconditions.require(similarity, "similarity must not be null"),
            loanProductBadges
        );
    }

    public static RecommendedLoanProduct rebuild(
        UUID recommendedLoanProductId,
        Long profileId,
        String bankName,
        Long loanProductId,
        String productName,
        Float maxInterestRate,
        Float minInterestRate,
        Long maxLoanLimitAmount,
        Float similarity,
        List<LoanProductBadge> loanProductBadges
    ) {
        InterestRate interestRate = new InterestRate(maxInterestRate, minInterestRate);

        return new RecommendedLoanProduct(
            Preconditions.require(recommendedLoanProductId, "recommendedLoanProductId must not be null"),
            Preconditions.require(profileId, "profileId must not be null"),
            Preconditions.require(bankName, "bankName must not be null"),
            Preconditions.require(loanProductId, "loanProductId must not be null"),
            Preconditions.require(productName, "productName must not be null"),
            interestRate,
            maxLoanLimitAmount,
            Preconditions.require(similarity, "similarity must not be null"),
            loanProductBadges
        );
    }

    public Float getMinInterestRate() {
        return this.interestRate.minInterestRate();
    }
}
