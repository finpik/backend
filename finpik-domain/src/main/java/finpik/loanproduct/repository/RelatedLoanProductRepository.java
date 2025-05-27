package finpik.loanproduct.repository;

import finpik.loanproduct.entity.RelatedLoanProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatedLoanProductRepository {
    List<RelatedLoanProduct> getRelatedLoanProducts(Long productId);
}
