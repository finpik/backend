package finpik.loanproduct.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import finpik.loanproduct.entity.RelatedLoanProduct;

@Repository
public interface RelatedLoanProductRepository {
    List<RelatedLoanProduct> getRelatedLoanProducts(Long productId);
}
