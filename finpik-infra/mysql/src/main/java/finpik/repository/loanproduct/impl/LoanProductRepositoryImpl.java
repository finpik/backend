package finpik.repository.loanproduct.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import finpik.LoanProduct;
import finpik.RecommendedLoanProduct;
import finpik.RelatedLoanProduct;
import finpik.entity.enums.ImmediateDepositAvailable;
import finpik.entity.enums.LoanProductBadge;
import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import finpik.jpa.repository.history.userproductview.UserProductViewHistoryJpaRepository;
import finpik.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import finpik.jpa.repository.loanproduct.LoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.RecommendedLoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.projection.LoanProductProjection;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.loanproduct.LoanProductEntity;
import finpik.repository.loanproduct.LoanProductRepository;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class LoanProductRepositoryImpl implements LoanProductRepository {
    private final LoanProductJpaRepository loanProductJpaRepository;
    private final RecommendedLoanProductJpaRepository recommendedLoanProductJpaRepository;
    private final UserProductViewHistoryJpaRepository viewHistoryJpaRepository;

    @Override
    @Transactional
    public Optional<LoanProduct> findByIdWithDescription(Long loanProductId, Long profileId) {
        Optional<LoanProductProjection> loanProductEntity =
            loanProductJpaRepository.findByIdWithDescriptionAndBadge(loanProductId, profileId);

        return loanProductEntity.map(it ->
            it.loanProductEntity().toDomain(it.loanProductBadgeList())
        );
    }

    @Override
    public List<RecommendedLoanProduct> saveAllRecommendedLoanProduct(Long profileId, List<RecommendedLoanProduct> recommendedLoanProductList) {
        Map<Long, LoanProductEntity> idLoanProductEntityMap = loanProductJpaRepository
            .findAllById(
                recommendedLoanProductList.stream()
                    .map(RecommendedLoanProduct::getLoanProductId)
                    .toList()
            )
            .stream()
            .collect(toMap(LoanProductEntity::getId, loanProductEntity -> loanProductEntity));

        final Float globalExtremeMinInterestRate = getGlobalExtremeMinInterestRate(recommendedLoanProductList);

        final Float globalExtremeMaxSimilarity = getGlobalExtremeMaxSimilarity(recommendedLoanProductList);

        final Long globalExtremeMaxLoanLimitAmount = getGlobalExtremeMaxLoanLimitAmount(recommendedLoanProductList);

        // 3) 배지 계산 + 엔티티 변환
        List<RecommendedLoanProductEntity> entityList = recommendedLoanProductList.stream()
            .map(recommendedLoanProduct -> {
                Long loanProductId = recommendedLoanProduct.getLoanProductId();
                LoanProductEntity loanProductEntity = idLoanProductEntityMap.get(loanProductId);

                // 배지 계산
                List<LoanProductBadge> badges = buildBadges(
                    recommendedLoanProduct, loanProductEntity,
                    globalExtremeMinInterestRate,
                    globalExtremeMaxSimilarity,
                    globalExtremeMaxLoanLimitAmount
                );

                return RecommendedLoanProductEntity.of(
                    profileId,
                    loanProductEntity,
                    recommendedLoanProduct.getSimilarity(),
                    badges
                );
            })
            .toList();

        // 4) 저장 후, 저장된 recommendedId를 도메인으로 재구성
        Map<Long, Long> loanProductIdToRecommendedId = recommendedLoanProductJpaRepository
            .saveAll(entityList)
            .stream()
            .collect(toMap(
                e -> e.getLoanProductEntity().getId(),
                RecommendedLoanProductEntity::getId
            ));

        return recommendedLoanProductList.stream()
            .map(p -> RecommendedLoanProduct.rebuild(
                loanProductIdToRecommendedId.get(p.getLoanProductId()),
                p.getProfileId(),
                p.getBankName(),
                p.getLoanProductId(),
                p.getProductName(),
                p.getInterestRate().maxInterestRate(),
                p.getInterestRate().minInterestRate(),
                p.getMaxLoanLimitAmount(),
                p.getSimilarity()
            ))
            .toList();
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

    private static Long getGlobalExtremeMaxLoanLimitAmount(List<RecommendedLoanProduct> recommendedLoanProductList) {
        return recommendedLoanProductList.stream()
            .map(RecommendedLoanProduct::getMaxLoanLimitAmount)
            .filter(Objects::nonNull)
            .max(Long::compare)
            .orElse(null);
    }

    private static Float getGlobalExtremeMaxSimilarity(List<RecommendedLoanProduct> recommendedLoanProductList) {
        return recommendedLoanProductList.stream()
            .map(RecommendedLoanProduct::getSimilarity)
            .filter(Objects::nonNull)
            .max(Float::compare)
            .orElse(null);
    }

    private Float getGlobalExtremeMinInterestRate(List<RecommendedLoanProduct> recommendedLoanProductList) {
        return recommendedLoanProductList.stream()
            .map(p -> p.getInterestRate() != null ? p.getInterestRate().minInterestRate() : null)
            .filter(Objects::nonNull)
            .min(Float::compare)
            .orElse(null);
    }

    /** 배지 계산: 동점 허용, null-세이프, 안정된 순서(ENUM 선언 순) 유지 */
    private static List<LoanProductBadge> buildBadges(
        RecommendedLoanProduct p,
        LoanProductEntity lp,
        Float globalMinOfMinInterest,
        Float globalMaxSimilarity,
        Long globalMaxLoanLimit
    ) {
        EnumSet<LoanProductBadge> set = EnumSet.noneOf(LoanProductBadge.class);

        if (globalMinOfMinInterest != null && p.getInterestRate() != null && p.getInterestRate().minInterestRate() != null) {
            if (Float.compare(p.getInterestRate().minInterestRate(), globalMinOfMinInterest) == 0) {
                set.add(LoanProductBadge.LOWEST_MIN_INTEREST_RATE);
            }
        }

        if (globalMaxSimilarity != null && p.getSimilarity() != null) {
            if (Float.compare(p.getSimilarity(), globalMaxSimilarity) == 0) {
                set.add(LoanProductBadge.BEST_PROFILE_MATCH);
            }
        }

        if (
            globalMaxLoanLimit != null
            && p.getMaxLoanLimitAmount() != null
            && Objects.equals(p.getMaxLoanLimitAmount(), globalMaxLoanLimit)
        ) {
            set.add(LoanProductBadge.HIGHEST_MAX_LOAN_AMOUNT_LIMIT);
        }


        if (lp != null && lp.getImmediateDepositAvailable() == ImmediateDepositAvailable.YES) {
            set.add(LoanProductBadge.INSTANT_DEPOSIT);
        }

        return List.copyOf(set);
    }
}
