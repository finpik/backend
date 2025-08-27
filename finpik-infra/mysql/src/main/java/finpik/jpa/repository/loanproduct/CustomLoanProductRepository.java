package finpik.jpa.repository.loanproduct;

import java.util.Optional;

import finpik.jpa.repository.loanproduct.projection.LoanProductProjection;

public interface CustomLoanProductRepository {
    Optional<LoanProductProjection> findByIdWithDescriptionAndBadge(Long loanProductId, Long profileId);
}
