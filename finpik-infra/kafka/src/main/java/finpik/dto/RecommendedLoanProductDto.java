package finpik.dto;

import java.util.List;

import finpik.service.loanproduct.dto.CachedRecommendedLoanProduct;

public record RecommendedLoanProductDto(Long profileId, List<RecommendedLoanProductResult> resultList) {

    record RecommendedLoanProductResult() {
        CachedRecommendedLoanProduct toCachedRecommendedLoanProduct() {
            return CachedRecommendedLoanProduct.builder().build();
        }
    }

    public List<CachedRecommendedLoanProduct> toCachedRecommendedLoanProductList() {
        return resultList.stream().map(RecommendedLoanProductResult::toCachedRecommendedLoanProduct).toList();
    }
}
