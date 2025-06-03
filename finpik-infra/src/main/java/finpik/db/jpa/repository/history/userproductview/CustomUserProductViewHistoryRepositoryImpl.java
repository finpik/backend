package finpik.db.jpa.repository.history.userproductview;

import java.util.List;

import finpik.db.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.db.entity.history.userproductview.QUserProductViewHistoryEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomUserProductViewHistoryRepositoryImpl implements CustomUserProductViewHistoryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private static final int RELATED_LOAN_PRODUCT_SEARCH_LIMIT = 3;

    @Override
    public List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId) {
        QUserProductViewHistoryEntity upvh1 = QUserProductViewHistoryEntity.userProductViewHistoryEntity;
        QUserProductViewHistoryEntity upvh2 = new QUserProductViewHistoryEntity("upvh2");

        return jpaQueryFactory.select(Projections.constructor(RelatedLoanProductProjection.class, upvh1)).from(upvh1)
                .join(upvh2).on(upvh1.userId.eq(upvh2.userId))
                .where(upvh1.productId.eq(productId), upvh2.productId.ne(productId)).groupBy(upvh2.productId)
                .orderBy(upvh2.productId.desc()).limit(RELATED_LOAN_PRODUCT_SEARCH_LIMIT).fetch();
    };
}
