package finpik.resolver.loanproduct.application;

import java.util.List;

import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;

public interface GetLoanProductUseCase {
    List<RecommendedLoanProductDto> getRecommendedLoanProducts(Long profileId);

    LoanProductDto getLoanProduct(Long loanProductId);

    List<RelatedLoanProductDto> getRelatedLoanProductList(Long productId);
}
