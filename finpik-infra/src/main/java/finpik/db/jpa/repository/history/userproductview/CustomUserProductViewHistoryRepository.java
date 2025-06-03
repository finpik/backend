package finpik.db.jpa.repository.history.userproductview;

import java.util.List;

import finpik.db.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserProductViewHistoryRepository {
    List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId);
}
