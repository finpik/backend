package finpik.jpa.repository.loanproduct.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    public Slice<RecommendedLoanProductProjection> findAllByProfileId(Long productId, Pageable pageable) {
        List<RecommendedLoanProductProjection> proejctionList = jpaQueryFactory
            .select(Projections.constructor(RecommendedLoanProductProjection.class,
                    recommendedLoanProductEntity.id,
                    recommendedLoanProductEntity.profileId,
                    loanProductEntity.bankName,
                    loanProductEntity.id,
                    loanProductEntity.productName,
                    loanProductEntity.maxInterestRate,
                    loanProductEntity.minInterestRate,
                    loanProductEntity.maxLoanLimitAmount,
                    recommendedLoanProductEntity.similarity,
                    recommendedLoanProductEntity.loanProductBadgeList
                ))
            .from(recommendedLoanProductEntity)
            .where(recommendedLoanProductEntity.profileId.eq(productId))
            .leftJoin(loanProductEntity).on(recommendedLoanProductEntity.loanProductEntity.id.eq(loanProductEntity.id))
            .offset(pageable.getOffset())
            .limit(getLimit(pageable.getPageSize()))
            .orderBy(orderBySort(pageable.getSort()))
            .fetch();

        boolean hasNext = hasNext(proejctionList.size(), pageable.getPageSize());

        if (hasNext) {
            proejctionList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(proejctionList, pageable, hasNext);
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
