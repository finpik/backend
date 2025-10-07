package finpik.repository.loanproduct.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
import jakarta.persistence.EntityManager;
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
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanProduct> findByIdWithDescription(Long loanProductId, Long profileId) {
        Optional<LoanProductProjection> loanProductEntity =
            loanProductJpaRepository.findByIdWithDescriptionAndBadge(loanProductId, profileId);

        return loanProductEntity.map(it ->
            it.loanProductEntity().toDomain(it.loanProductBadgeList())
        );
    }

    @Override
    @Transactional
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

        List<RecommendedLoanProductEntity> entityList = recommendedLoanProductList.stream()
            .map(recommendedLoanProduct -> {
                Long loanProductId = recommendedLoanProduct.getLoanProductId();
                LoanProductEntity loanProductEntity = idLoanProductEntityMap.get(loanProductId);

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
            }).toList();

        //배치 처리로 메모리에 부담을 적게 주기위한 배치 메서드
        saveAllRecommendedLoanProductInBatch(entityList);

        List<RecommendedLoanProductEntity> savedEntities = recommendedLoanProductJpaRepository.findAllByProfileId(profileId);

        return toDomainFrom(savedEntities, recommendedLoanProductList);
    }

    private void saveAllRecommendedLoanProductInBatch(
        List<RecommendedLoanProductEntity> rows
    ) {
        int batchSize = 500;
        for (int start = 0; start < rows.size(); start += batchSize) {
            int end = Math.min(start + batchSize, rows.size());
            List<RecommendedLoanProductEntity> chunk = rows.subList(start, end);

            for (RecommendedLoanProductEntity r : chunk) {
                em.persist(r);
            }

            em.flush();
            em.clear();
        }
    }

    private List<RecommendedLoanProduct> toDomainFrom(
        List<RecommendedLoanProductEntity> entityList,
        List<RecommendedLoanProduct> recommendedLoanProductList
    ) {
        Map<Long, RecommendedLoanProductEntity> loanProductIdToRecommendedId = entityList.stream()
            .collect(toMap(
                e -> e.getLoanProductEntity().getId(),
                e -> e
            ));

        return recommendedLoanProductList.stream()
            .map(p -> {
                RecommendedLoanProductEntity entity = loanProductIdToRecommendedId.get(p.getLoanProductId());

                return RecommendedLoanProduct.rebuild(
                    entity.getId(),
                    p.getProfileId(),
                    p.getBankName(),
                    p.getLoanProductId(),
                    p.getProductName(),
                    p.getInterestRate().maxInterestRate(),
                    p.getInterestRate().minInterestRate(),
                    p.getMaxLoanLimitAmount(),
                    p.getSimilarity(),
                    entity.getLoanProductBadgeList()
                );
            })
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<RecommendedLoanProduct> findAllRecommendedLoanProductSliceByProfileId(Long profileId, Pageable pageable) {
        Slice<RecommendedLoanProductProjection> recommendedLoanProductList =
            recommendedLoanProductJpaRepository.findAllByProfileIdPage(profileId, pageable);

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
                    product.similarity(),
                    product.loanProductBadges()
                )
            )
            .toList();

        return new SliceImpl<>(content, pageable, recommendedLoanProductList.hasNext());
    }

    @Override
    @Transactional(readOnly = true)
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
