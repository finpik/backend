package finpik.repository.loanproduct;

import finpik.RecommendedLoanProduct;

import java.util.List;


public interface LoanProductCacheRepository {
    List<RecommendedLoanProduct> cacheRecommendations(
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    );

    List<RecommendedLoanProduct> findAllById(Long profileId);
}
