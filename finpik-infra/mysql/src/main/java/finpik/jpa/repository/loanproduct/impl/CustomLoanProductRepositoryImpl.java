package finpik.jpa.repository.loanproduct.impl;

import static com.querydsl.core.types.dsl.Expressions.enumPath;
import static finpik.entity.loanproduct.QLoanProductDescriptionEntity.loanProductDescriptionEntity;
import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;
import static finpik.entity.loanproduct.QRecommendedLoanProductEntity.recommendedLoanProductEntity;

import java.util.Map;
import java.util.Optional;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.entity.enums.LoanProductBadge;
import finpik.jpa.repository.loanproduct.CustomLoanProductRepository;
import finpik.jpa.repository.loanproduct.projection.LoanProductProjection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoanProductRepositoryImpl implements CustomLoanProductRepository {
    private static final String LOAN_PRODUCT_BADGE_ALIAS = "badge";

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LoanProductProjection> findByIdWithDescriptionAndBadge(Long loanProductId, Long profileId) {
        EnumPath<LoanProductBadge> badge = enumPath(LoanProductBadge.class, LOAN_PRODUCT_BADGE_ALIAS);

        Map<Long, LoanProductProjection> result = jpaQueryFactory
            .from(loanProductEntity)
            .leftJoin(loanProductEntity.description, loanProductDescriptionEntity).fetchJoin()
            .leftJoin(recommendedLoanProductEntity)
            .on(recommendedLoanProductEntity.loanProductEntity.eq(loanProductEntity)
                .and(recommendedLoanProductEntity.profileId.eq(profileId)))
            .leftJoin(recommendedLoanProductEntity.loanProductBadgeList, badge)
            .where(loanProductEntity.id.eq(loanProductId))
            .transform(GroupBy.groupBy(loanProductEntity.id).as(
                Projections.constructor(
                    LoanProductProjection.class,
                    loanProductEntity,
                    GroupBy.list(badge)
                )
            ));

        return Optional.ofNullable(result.get(loanProductId));
    }
}
