package finpik.db.jpa.repository.history;

import finpik.db.jpa.repository.history.dto.RelatedLoanProductProjection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserProductViewHistoryRepository {
    List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId);
}
