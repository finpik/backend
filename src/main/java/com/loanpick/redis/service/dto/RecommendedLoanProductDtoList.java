package com.loanpick.redis.service.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record RecommendedLoanProductDtoList(List<RecommendedLoanProductDto> dtos) {
}
