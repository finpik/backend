package com.loanpick.loanproduct.resolver.result;

import com.loanpick.redis.service.dto.RecommendedLoanProductDto;

import lombok.Builder;

@Builder
public record RecommendedLoanProductResult(Long loanProductId, String productName, Float minInterestRate,
        Float maxInterestRate, Integer loanLimitAmount) {
    public static RecommendedLoanProductResult of(RecommendedLoanProductDto dto) {
        return RecommendedLoanProductResult.builder().loanProductId(dto.id()).productName(dto.productName())
                .minInterestRate(dto.minInterestRate()).maxInterestRate(dto.maxInterestRate())
                .loanLimitAmount(dto.loanLimitAmount()).build();
    }
}
