package finpik.jpa.repository.history.userproductview;

import java.util.List;

import org.springframework.stereotype.Repository;

import finpik.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;

@Repository
public interface CustomUserProductViewHistoryRepository {
    List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId);
}
