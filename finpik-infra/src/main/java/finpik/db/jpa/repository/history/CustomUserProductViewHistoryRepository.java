package finpik.db.jpa.repository.history;

import java.util.List;

import org.springframework.stereotype.Repository;

import finpik.db.jpa.repository.history.dto.RelatedLoanProductProjection;

@Repository
public interface CustomUserProductViewHistoryRepository {
    List<RelatedLoanProductProjection> getRelatedLoanProductList(Long productId);
}
