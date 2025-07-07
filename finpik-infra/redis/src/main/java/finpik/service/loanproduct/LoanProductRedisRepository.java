package finpik.service.loanproduct;

import static finpik.util.RedisKeyValues.RECOMMENDATION_KEY;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import finpik.service.loanproduct.dto.CachedRecommendedLoanProduct;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProductList;
import lombok.RequiredArgsConstructor;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LoanProductRedisRepository {
    @CachePut(value = RECOMMENDATION_KEY, key = "#profileId")
    public CachedRecommendedLoanProductList cacheRecommendations(Long profileId,
            List<CachedRecommendedLoanProduct> recommendations) {
        log.info("레디스 호출됨 key: " + RECOMMENDATION_KEY + " profileId: " + profileId);

        return new CachedRecommendedLoanProductList(recommendations);
    }

    @Cacheable(value = RECOMMENDATION_KEY, key = "#profileId")
    public CachedRecommendedLoanProductList getRecommendations(Long profileId) {
        return new CachedRecommendedLoanProductList(Collections.emptyList());
    }
}
