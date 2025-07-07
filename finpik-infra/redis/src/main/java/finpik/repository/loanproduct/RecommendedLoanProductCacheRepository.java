package finpik.repository.loanproduct;

import finpik.loanproduct.RecommendedLoanProduct;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface RecommendedLoanProductCacheRepository {
    CompletableFuture<List<RecommendedLoanProduct>> cacheAsync(
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    );

    List<RecommendedLoanProduct> findAllById(Long profileId);
}
