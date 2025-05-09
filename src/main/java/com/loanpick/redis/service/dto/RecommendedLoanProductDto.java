package com.loanpick.redis.service.dto;

import lombok.Builder;

@Builder
public record RecommendedLoanProductDto(Long id, String productName, Float minInterestRate, Float maxInterestRate,
        Integer loanLimitAmount) {
}
