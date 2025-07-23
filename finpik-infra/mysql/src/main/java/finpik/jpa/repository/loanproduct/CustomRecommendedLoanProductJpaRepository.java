package finpik.jpa.repository.loanproduct;

import finpik.jpa.repository.loanproduct.projection.RecommendedLoanProductProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomRecommendedLoanProductJpaRepository {
    Slice<RecommendedLoanProductProjection> findAllByProfileId(Long productId, Pageable pageable);
}
