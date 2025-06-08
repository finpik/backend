package finpik.repository.loanproduct;

import java.util.List;

import org.springframework.stereotype.Repository;

import finpik.loanproduct.RecommendedLoanProduct;
import finpik.service.loanproduct.LoanProductRedisRepository;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProduct;
import finpik.service.loanproduct.dto.CachedRecommendedLoanProductList;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoanProductCacheRepositoryImpl implements LoanProductCacheRepository {
    private final LoanProductRedisRepository loanProductRedisRepository;

    @Override
    public List<RecommendedLoanProduct> cacheRecommendations(Long profileId,
            List<RecommendedLoanProduct> recommendedLoanProductList) {
        List<CachedRecommendedLoanProduct> dtoList = recommendedLoanProductList.stream()
                .map(CachedRecommendedLoanProduct::from).toList();

        CachedRecommendedLoanProductList resultList = loanProductRedisRepository.cacheRecommendations(profileId,
                dtoList);

        return resultList.dtos().stream().map(CachedRecommendedLoanProduct::toDomain).toList();
    }

    @Override
    public List<RecommendedLoanProduct> findAllById(Long profileId) {
        CachedRecommendedLoanProductList resultList = loanProductRedisRepository.getRecommendations(profileId);

        return resultList.dtos().stream().map(CachedRecommendedLoanProduct::toDomain).toList();
    }
}
