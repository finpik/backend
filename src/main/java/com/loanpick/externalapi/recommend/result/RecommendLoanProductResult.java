package com.loanpick.externalapi.recommend.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loanpick.redis.service.dto.RecommendedLoanProductDto;

public record RecommendLoanProductResult(Long id, @JsonProperty("name") String productName,
        @JsonProperty("min_interest_rate") Float minInterestRate,
        @JsonProperty("max_interest_rate") Float maxInterestRate,
        @JsonProperty("loan_limit_amount") Integer loanLimitAmount) {

    public RecommendedLoanProductDto toDto() {
        return RecommendedLoanProductDto.builder().id(id).productName(productName).minInterestRate(minInterestRate)
                .maxInterestRate(maxInterestRate).loanLimitAmount(loanLimitAmount).build();
    }
}
