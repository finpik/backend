package finpik.loanproduct.service;

import java.util.List;

import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.RecommendedLoanProduct;

public interface LoanProductService {
    LoanProduct getLoanProduct(Long id);

    List<RecommendedLoanProduct> getRecommendedLoanProducts(Long profileId);
}
