package finpik.resolver.loanproduct.resolver.result;

import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RecommendedLoanProductResult {
    private final UUID recommendedLoanProductId;
    private final Long profileId;
    private final String bankName;
    private final Long loanProductId;
    private final String productName;
    private final Float minInterestRate;
    private final Float maxInterestRate;
    private final Long maxLoanLimitAmount;

    public RecommendedLoanProductResult (RecommendedLoanProductDto dto) {
        recommendedLoanProductId = dto.getRecommendedLoanProductId();
        profileId = dto.getProfileId();
        bankName = dto.getBankName();
        loanProductId = dto.getLoanProductId();
        productName = dto.getProductName();
        maxInterestRate = dto.getMinInterestRate();
        minInterestRate = dto.getMaxInterestRate();
        maxLoanLimitAmount = dto.getMaxLoanLimitAmount();
    }
}
