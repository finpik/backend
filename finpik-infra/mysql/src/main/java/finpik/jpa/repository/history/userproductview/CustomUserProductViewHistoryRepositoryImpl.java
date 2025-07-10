package finpik.jpa.repository.history.userproductview;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import lombok.RequiredArgsConstructor;

import static finpik.entity.history.userproductview.QUserProductViewHistoryEntity.userProductViewHistoryEntity;


@Repository
@RequiredArgsConstructor
public class CustomUserProductViewHistoryRepositoryImpl implements CustomUserProductViewHistoryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private static final int RELATED_LOAN_PRODUCT_SEARCH_LIMIT = 3;

    @Override
    public List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId) {
        JPQLQuery<Long> userIdSubquery = JPAExpressions
            .select(userProductViewHistoryEntity.userId)
            .from(userProductViewHistoryEntity)
            .where(userProductViewHistoryEntity.loanProductId.eq(productId));


        return jpaQueryFactory
            .select(Projections.constructor(
                RelatedLoanProductProjection.class,
                userProductViewHistoryEntity.loanProductId
            ))
            .from(userProductViewHistoryEntity)
            .where(
                userProductViewHistoryEntity.userId.in(userIdSubquery),
                userProductViewHistoryEntity.loanProductId.ne(productId)
            )
            .groupBy(userProductViewHistoryEntity.loanProductId)
            .orderBy(userProductViewHistoryEntity.loanProductId.count().desc())
            .limit(RELATED_LOAN_PRODUCT_SEARCH_LIMIT)
            .fetch();
    }
}
