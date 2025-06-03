package finpik.jpa.repository.loanproduct;

import java.util.Optional;

import finpik.entity.loanproduct.LoanProductEntity;

public interface CustomLoanProductRepository {
    Optional<LoanProductEntity> findByIdWithDescription(Long loanProductId);
}
