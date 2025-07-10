package finpik.repository.loanproduct;

import java.util.Optional;

import finpik.LoanProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanProductRepository {
    Optional<LoanProduct> findByIdWithDescription(Long loanProductId);
}
