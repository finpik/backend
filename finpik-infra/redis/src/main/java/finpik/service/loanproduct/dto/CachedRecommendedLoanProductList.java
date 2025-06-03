package finpik.service.loanproduct.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CachedRecommendedLoanProductList(List<CachedRecommendedLoanProduct> dtos) {
}
