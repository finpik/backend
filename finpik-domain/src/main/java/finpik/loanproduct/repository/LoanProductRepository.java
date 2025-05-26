package finpik.loanproduct.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import finpik.loanproduct.entity.LoanProduct;

@Repository
public interface LoanProductRepository {
    Optional<LoanProduct> findByIdWithDescription(Long loanProductId);
}
