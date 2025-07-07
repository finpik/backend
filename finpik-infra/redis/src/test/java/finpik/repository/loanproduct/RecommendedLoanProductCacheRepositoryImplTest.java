package finpik.repository.loanproduct;

import finpik.loanproduct.RecommendedLoanProduct;
import finpik.service.loanproduct.LoanProductRedisRepository;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProduct;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProductList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RecommendedLoanProductCacheRepositoryImplTest {
    @Autowired
    private RecommendedLoanProductCacheRepositoryImpl repository;

    @Autowired
    private LoanProductRedisRepository redisRepository;

    @Test
    void cacheAsync_실제_redis에_캐싱된다() throws Exception {
        // given
        Long profileId = 123L;
        RecommendedLoanProduct product = RecommendedLoanProduct.rebuild(
            null, profileId, 200L, "📦 캐시 테스트 상품", 4.9f, 3.2f, 50000000L
        );

        // when
        CompletableFuture<List<RecommendedLoanProduct>> resultFuture =
            repository.cacheAsync(profileId, List.of(product));
        List<RecommendedLoanProduct> result = resultFuture.get();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLoanProductId()).isEqualTo(200L);
        assertThat(result.get(0).getProductName()).contains("캐시 테스트");

        // redis에 실제 저장되었는지 확인
        CachedRecommendedLoanProductList cached = redisRepository.cacheRecommendations(profileId,
            List.of(CachedRecommendedLoanProduct.from(product)));

        assertThat(cached.dtos()).hasSize(1);
        assertThat(cached.dtos().getFirst().getLoanProductId()).isEqualTo(200L);
    }
}
