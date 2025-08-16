package finpik.repository.loanproduct.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import finpik.LoanProduct;
import finpik.RecommendedLoanProduct;
import finpik.RelatedLoanProduct;
import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import finpik.jpa.repository.history.userproductview.UserProductViewHistoryJpaRepository;
import finpik.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import finpik.jpa.repository.loanproduct.LoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.RecommendedLoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.loanproduct.LoanProductEntity;
import finpik.repository.loanproduct.LoanProductRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoanProductRepositoryImpl implements LoanProductRepository {
    private final LoanProductJpaRepository loanProductJpaRepository;
    private final RecommendedLoanProductJpaRepository recommendedLoanProductJpaRepository;
    private final UserProductViewHistoryJpaRepository viewHistoryJpaRepository;

    @Override
    @Transactional
    public Optional<LoanProduct> findByIdWithDescription(Long loanProductId) {
        Optional<LoanProductEntity> loanProductEntity = loanProductJpaRepository.findByIdWithDescription(loanProductId);

        return loanProductEntity.map(LoanProductEntity::toDomain);
    }

    @Override
    public List<RecommendedLoanProduct> saveAllRecommendedLoanProduct(Long profileId, List<RecommendedLoanProduct> recommendedLoanProductList) {
        List<RecommendedLoanProductEntity> entityList = recommendedLoanProductList.stream().map(product ->
            RecommendedLoanProductEntity.of(
                profileId,
                product.getLoanProductId(),
                product.getSimilarity()
            )
        ).toList();

        Map<Long, Long> loanProductIdMap = recommendedLoanProductJpaRepository.saveAll(entityList).stream().collect(
            Collectors.toMap(RecommendedLoanProductEntity::getLoanProductId, RecommendedLoanProductEntity::getId)
        );

        return recommendedLoanProductList.stream().map(product ->
            RecommendedLoanProduct.rebuild(
                loanProductIdMap.get(product.getLoanProductId()),
                product.getProfileId(),
                product.getBankName(),
                product.getLoanProductId(),
                product.getProductName(),
                product.getInterestRate().maxInterestRate(),
                product.getInterestRate().minInterestRate(),
                product.getMaxLoanLimitAmount(),
                product.getSimilarity()
            )
        ).toList();
    }

    @Override
    public Slice<RecommendedLoanProduct> findAllRecommendedLoanProductSliceByProfileId(Long profileId, Pageable pageable) {
        Slice<RecommendedLoanProductProjection> recommendedLoanProductList =
            recommendedLoanProductJpaRepository.findAllByProfileId(profileId, pageable);

        List<RecommendedLoanProduct> content = recommendedLoanProductList
            .stream()
            .map(product ->
                RecommendedLoanProduct.rebuild(
                    product.recommendedLoanProductId(),
                    product.profileId(),
                    product.bankName(),
                    product.loanProductId(),
                    product.productName(),
                    product.maxInterestRate(),
                    product.minInterestRate(),
                    product.maxLoanLimitAmount(),
                    product.similarity()
                )
            )
            .toList();

        return new SliceImpl<>(content, pageable, recommendedLoanProductList.hasNext());
    }

    @Override
    public List<RelatedLoanProduct> findAllRelatedLoanProductById(Long loanProductId) {
        List<RelatedLoanProductProjection> projectionList = viewHistoryJpaRepository
            .getRelatedLoanProductList(loanProductId);

        List<Long> relatedLoanProductIdList = projectionList.stream().map(RelatedLoanProductProjection::loanProductId)
            .toList();

        List<LoanProductEntity> entityList = loanProductJpaRepository.findAllById(relatedLoanProductIdList);

        return entityList.stream()
            .map(entity ->
                RelatedLoanProduct.of(
                    entity.getId(),
                    entity.getProductName(),
                    entity.getBankName(),
                    entity.getMaxInterestRate(),
                    entity.getMinInterestRate(),
                    entity.getMaxLoanLimitAmount()
                )
            ).toList();
    }

    @Override
    public List<LoanProduct> updateAll(List<LoanProduct> loanProductList) {
        List<LoanProductEntity> entities =
            loanProductJpaRepository.findAllById(loanProductList.stream().map(LoanProduct::getId).toList());

        Map<Long, LoanProduct> loanProductIdLoanProductMap = loanProductList.stream().collect(
            Collectors.toMap(LoanProduct::getId, loanProduct -> loanProduct
        ));

        entities.forEach(entity -> {
            LoanProduct loanProduct = loanProductIdLoanProductMap.get(entity.getId());

            entity.changeLoanProductBadgeListAndPrerequisite(
                loanProduct.getLoanProductBadgeList(),
                loanProduct.getDescription().getLoanPrerequisite()
            );
        });

        loanProductJpaRepository.saveAll(entities);

        return entities.stream().map(LoanProductEntity::toDomain).toList();
    }

    @Override
    public List<LoanProduct> findAllById(List<Long> ids) {
        return loanProductJpaRepository.findAllById(ids).stream().map(LoanProductEntity::toDomain).toList();
    }
}
