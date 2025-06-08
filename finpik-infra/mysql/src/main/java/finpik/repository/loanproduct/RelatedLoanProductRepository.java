package finpik.repository.loanproduct;

import java.util.List;

import finpik.loanproduct.RelatedLoanProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatedLoanProductRepository {
    List<RelatedLoanProduct> findAllById(Long productId);
}
