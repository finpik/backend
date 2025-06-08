package finpik.resolver.loanproduct.application;

import java.util.List;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.loanproduct.LoanProductCacheRepository;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.repository.loanproduct.RelatedLoanProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.loanproduct.LoanProduct;
import finpik.loanproduct.RecommendedLoanProduct;
import finpik.loanproduct.RelatedLoanProduct;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetLoanProductUseCase {
    private final LoanProductRepository loanProductRepository;
    private final RelatedLoanProductRepository relatedLoanProductRepository;
    private final LoanProductCacheRepository loanProductCacheRepository;

    @Transactional(readOnly = true)
    public List<RecommendedLoanProductDto> getRecommendedLoanProducts(Long profileId) {
        List<RecommendedLoanProduct> recommendedLoanProducts = loanProductCacheRepository.findAllById(profileId);

        return recommendedLoanProducts.stream().map(RecommendedLoanProductDto::new).toList();
    }

    @Transactional(readOnly = true)
    public LoanProductDto getLoanProduct(Long loanProductId) {
        LoanProduct loanProduct = loanProductRepository.findByIdWithDescription(loanProductId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOAN_PRODUCT));

        return new LoanProductDto(loanProduct);
    }

    @Transactional(readOnly = true)
    public List<RelatedLoanProductDto> getRelatedLoanProductList(Long productId) {
        List<RelatedLoanProduct> relatedLoanProductList = relatedLoanProductRepository.findAllById(productId);

        return relatedLoanProductList.stream().map(RelatedLoanProductDto::new).toList();
    }
}
