package finpik.repository.loanproduct;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import finpik.loanproduct.RecommendedLoanProduct;
import finpik.service.loanproduct.LoanProductRedisRepository;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProduct;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProductList;
import lombok.RequiredArgsConstructor;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RecommendedLoanProductCacheRepositoryImpl implements RecommendedLoanProductCacheRepository {
    private final LoanProductRedisRepository loanProductRedisRepository;

    @Async
    @Override
    public CompletableFuture<List<RecommendedLoanProduct>> cacheAsync(
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    ) {
        List<CachedRecommendedLoanProduct> dtoList = recommendedLoanProductList.stream()
                .map(CachedRecommendedLoanProduct::from).toList();

        CachedRecommendedLoanProductList resultList = loanProductRedisRepository.cacheRecommendations(profileId,
                dtoList);

        return CompletableFuture.completedFuture(resultList.dtos().stream().map(CachedRecommendedLoanProduct::toDomain).toList());
    }

    @Override
    public List<RecommendedLoanProduct> findAllById(Long profileId) {
        CachedRecommendedLoanProductList resultList = loanProductRedisRepository.getRecommendations(profileId);

        if (resultList.dtos().isEmpty()) {
            return Collections.emptyList();
        }

        return resultList.dtos().stream().map(CachedRecommendedLoanProduct::toDomain).toList();
    }
}
