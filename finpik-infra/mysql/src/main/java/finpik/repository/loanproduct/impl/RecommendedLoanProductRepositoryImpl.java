package finpik.repository.loanproduct.impl;

import finpik.RecommendedLoanProduct;
import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import finpik.jpa.repository.loanproduct.RecommendedLoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import finpik.repository.loanproduct.RecommendedLoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecommendedLoanProductRepositoryImpl implements RecommendedLoanProductRepository {
    private final RecommendedLoanProductJpaRepository jpaRepository;

    public List<RecommendedLoanProduct> saveAll(Long profileId, List<RecommendedLoanProduct> recommendedLoanProductList) {
        List<RecommendedLoanProductEntity> entityList = recommendedLoanProductList.stream().map(product ->
            RecommendedLoanProductEntity.of(
                profileId,
                product.getLoanProductId()
            )
        ).toList();

        Map<Long, Long> loanProductIdMap = jpaRepository.saveAll(entityList).stream().collect(
            Collectors.toMap(RecommendedLoanProductEntity::getLoanProductId, RecommendedLoanProductEntity::getId)
        );

        return recommendedLoanProductList.stream().map(product ->
            RecommendedLoanProduct.rebuild(
                loanProductIdMap.get(product.getLoanProductId()),
                product.getProfileId(),
                product.getLoanProductId(),
                product.getProductName(),
                product.getInterestRate().maxInterestRate(),
                product.getInterestRate().minInterestRate(),
                product.getMaxLoanLimitAmount()
            )
        ).toList();
    }

    @Override
    public List<RecommendedLoanProduct> findAllByProfileId(Long profileId) {
        List<RecommendedLoanProductProjection> recommendedLoanProductList =
            jpaRepository.findAllByProfileId(profileId);

        return recommendedLoanProductList.stream().map(product ->
            RecommendedLoanProduct.rebuild(
                product.recommendedLoanProductId(),
                product.profileId(),
                product.loanProductId(),
                product.productName(),
                product.maxInterestRate(),
                product.minInterestRate(),
                product.maxLoanLimitAmount()
            )
        ).toList();
    }
}
