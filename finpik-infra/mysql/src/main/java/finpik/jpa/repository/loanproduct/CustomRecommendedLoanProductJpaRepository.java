package finpik.jpa.repository.loanproduct;

import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;

import java.util.List;

public interface CustomRecommendedLoanProductJpaRepository {
    List<RecommendedLoanProductProjection> findAllByProfileId(Long productId);
}
