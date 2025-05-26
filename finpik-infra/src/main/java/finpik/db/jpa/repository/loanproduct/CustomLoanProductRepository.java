package finpik.db.jpa.repository.loanproduct;

import java.util.Optional;

import finpik.db.entity.loanproduct.LoanProductEntity;

public interface CustomLoanProductRepository {
    Optional<LoanProductEntity> findByIdWithDescription(Long loanProductId);
}
