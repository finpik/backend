package finpik.jpa.repository.loanproduct.impl;

import static com.querydsl.core.types.dsl.Expressions.enumPath;
import static finpik.entity.loanproduct.QLoanProductDescriptionEntity.loanProductDescriptionEntity;
import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;
import static finpik.entity.loanproduct.QRecommendedLoanProductEntity.recommendedLoanProductEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.entity.enums.LoanProductBadge;
import finpik.entity.loanproduct.LoanProductEntity;
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

        LoanProductEntity loanProductEntityByRecommended = jpaQueryFactory
            .selectFrom(loanProductEntity)
            .leftJoin(loanProductEntity.description, loanProductDescriptionEntity).fetchJoin()
            .where(loanProductEntity.id.eq(loanProductId))
            .fetchOne();

        if (loanProductEntityByRecommended == null) return Optional.empty();

        List<LoanProductBadge> badges = jpaQueryFactory
            .select(badge)
            .from(recommendedLoanProductEntity)
            .leftJoin(recommendedLoanProductEntity.loanProductBadgeList, badge)
            .where(recommendedLoanProductEntity.loanProductEntity.eq(loanProductEntityByRecommended)
                .and(recommendedLoanProductEntity.profileId.eq(profileId)))
            .fetch();

        return Optional.of(new LoanProductProjection(loanProductEntityByRecommended, badges));
    }
}
