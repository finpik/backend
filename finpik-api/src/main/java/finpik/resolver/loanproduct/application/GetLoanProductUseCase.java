package finpik.resolver.loanproduct.application;

import java.util.List;

import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GetLoanProductUseCase {
    Slice<RecommendedLoanProductDto> getRecommendedLoanProductList(Long profileId, Pageable pageable);

    LoanProductDto getLoanProduct(Long loanProductId);

    List<RelatedLoanProductDto> getRelatedLoanProductList(Long productId);
}
