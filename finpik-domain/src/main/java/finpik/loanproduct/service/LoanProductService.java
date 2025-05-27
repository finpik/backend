package finpik.loanproduct.service;

import java.util.List;

import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.RecommendedLoanProduct;
import finpik.loanproduct.entity.RelatedLoanProduct;

public interface LoanProductService {
    LoanProduct getLoanProduct(Long id);

    List<RecommendedLoanProduct> getRecommendedLoanProducts(Long profileId);

    List<RelatedLoanProduct> getRelatedLoanProductList(Long productId);
}
