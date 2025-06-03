package finpik.redis.service.loanproduct;

import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import finpik.redis.service.loanproduct.dto.CachedRecommendedLoanProduct;
import finpik.redis.service.loanproduct.dto.CachedRecommendedLoanProductList;
import lombok.RequiredArgsConstructor;

import static finpik.util.RedisKeyValues.RECOMMENDATION_KEY;

@Repository
@RequiredArgsConstructor
public class LoanProductRedisRepository {
    @CachePut(value = RECOMMENDATION_KEY, key = "#profileId")
    public CachedRecommendedLoanProductList cacheRecommendations(Long profileId,
            List<CachedRecommendedLoanProduct> recommendations) {
        return new CachedRecommendedLoanProductList(recommendations);
    }

    @Cacheable(value = RECOMMENDATION_KEY, key = "#profileId")
    public CachedRecommendedLoanProductList getRecommendations(Long profileId) {
        return new CachedRecommendedLoanProductList(Collections.emptyList());
    }
}
