package finpik.resolver.loanproduct.application;

import java.util.List;

import finpik.loanproduct.entity.RelatedLoanProduct;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.RecommendedLoanProduct;
import finpik.loanproduct.service.LoanProductService;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanProductQueryService {
    private final LoanProductService loanProductService;

    @Transactional(readOnly = true)
    public List<RecommendedLoanProductDto> getRecommendedLoanProducts(Long profileId) {
        List<RecommendedLoanProduct> recommendedLoanProducts = loanProductService.getRecommendedLoanProducts(profileId);

        return recommendedLoanProducts.stream().map(RecommendedLoanProductDto::new).toList();
    }

    @Transactional(readOnly = true)
    public LoanProductDto getLoanProduct(Long loanProductId) {
        LoanProduct loanProduct = loanProductService.getLoanProduct(loanProductId);

        return new LoanProductDto(loanProduct);
    }

    @Transactional(readOnly = true)
    public List<RelatedLoanProductDto> getRelatedLoanProductList(Long productId) {
        List<RelatedLoanProduct> relatedLoanProductList = loanProductService.getRelatedLoanProductList(productId);

        return relatedLoanProductList.stream().map(RelatedLoanProductDto::new).toList();
    }
}
