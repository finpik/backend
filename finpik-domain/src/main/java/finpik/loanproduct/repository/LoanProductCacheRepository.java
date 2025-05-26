package finpik.loanproduct.repository;

import java.util.List;

import finpik.loanproduct.entity.RecommendedLoanProduct;

public interface LoanProductCacheRepository {
    List<RecommendedLoanProduct> cacheRecommendations(Long profileId,
            List<RecommendedLoanProduct> recommendedLoanProductList);

    List<RecommendedLoanProduct> getRecommendations(Long profileId);
}
