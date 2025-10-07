package finpik.jpa.repository.loanproduct.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import finpik.jpa.repository.loanproduct.CustomRecommendedLoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import finpik.util.db.OrderSpecifierTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.List;

import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;
import static finpik.entity.loanproduct.QRecommendedLoanProductEntity.recommendedLoanProductEntity;
import static finpik.util.db.QuerydslUtil.getLimit;
import static finpik.util.db.QuerydslUtil.hasNext;


@RequiredArgsConstructor
public class CustomRecommendedLoanProductJpaRepositoryImpl implements CustomRecommendedLoanProductJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private static final String SIMILARITY_PROPERTY = "similarity";

    public Slice<RecommendedLoanProductProjection> findAllByProfileIdPage(Long profileId, Pageable pageable) {
        List<RecommendedLoanProductEntity> recommendedLoanProducts = jpaQueryFactory
            .selectFrom(recommendedLoanProductEntity)
            .leftJoin(recommendedLoanProductEntity.loanProductEntity, loanProductEntity).fetchJoin()
            .leftJoin(recommendedLoanProductEntity.loanProductBadgeList).fetchJoin()
            .where(recommendedLoanProductEntity.profileId.eq(profileId))
            .orderBy(orderBySort(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(getLimit(pageable.getPageSize()))
            .distinct()
            .fetch();

        boolean hasNextPage = hasNext(recommendedLoanProducts.size(), pageable.getPageSize());
        if (hasNextPage) {
            recommendedLoanProducts.remove(pageable.getPageSize());
        }

        List<RecommendedLoanProductProjection> projections = recommendedLoanProducts.stream()
            .map(entity -> new RecommendedLoanProductProjection(
                entity.getId(),
                entity.getProfileId(),
                entity.getLoanProductEntity().getBankName(),
                entity.getLoanProductEntity().getId(),
                entity.getLoanProductEntity().getProductName(),
                entity.getLoanProductEntity().getMinInterestRate(),
                entity.getLoanProductEntity().getMaxInterestRate(),
                entity.getLoanProductEntity().getMaxLoanLimitAmount(),
                entity.getSimilarity(),
                entity.getLoanProductBadgeList()
            ))
            .toList();

        return new SliceImpl<>(projections, pageable, hasNextPage);
    }

    private OrderSpecifier<?>[] orderBySort(Sort sort) {
        if (sort.getOrderFor(SIMILARITY_PROPERTY) != null) {
            OrderSpecifierTransformer transformer = new OrderSpecifierTransformer(recommendedLoanProductEntity);
            return transformer.transform(sort);
        }

        OrderSpecifierTransformer transformer = new OrderSpecifierTransformer(loanProductEntity);
        return transformer.transform(sort);
    }
}
