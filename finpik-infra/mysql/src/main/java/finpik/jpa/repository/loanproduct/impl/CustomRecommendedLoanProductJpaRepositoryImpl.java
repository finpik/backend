package finpik.jpa.repository.loanproduct.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import finpik.jpa.repository.loanproduct.CustomRecommendedLoanProductJpaRepository;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;
import static finpik.entity.loanproduct.QRecommendedLoanProductEntity.recommendedLoanProductEntity;


@RequiredArgsConstructor
public class CustomRecommendedLoanProductJpaRepositoryImpl implements CustomRecommendedLoanProductJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<RecommendedLoanProductProjection> findAllByProfileId(Long productId) {
        return jpaQueryFactory
            .select(Projections.constructor(RecommendedLoanProductProjection.class,
                    recommendedLoanProductEntity.id,
                    recommendedLoanProductEntity.profileId,
                    recommendedLoanProductEntity.loanProductId,
                    loanProductEntity.productName,
                    loanProductEntity.maxInterestRate,
                    loanProductEntity.minInterestRate,
                    loanProductEntity.maxLoanLimitAmount
                ))
            .from(recommendedLoanProductEntity)
            .where(recommendedLoanProductEntity.profileId.eq(productId))
            .leftJoin(loanProductEntity).on(recommendedLoanProductEntity.loanProductId.eq(loanProductEntity.id))
            .fetch();
    }
}
