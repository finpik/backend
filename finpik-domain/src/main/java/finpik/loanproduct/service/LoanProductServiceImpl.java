package finpik.loanproduct.service;

import java.util.List;

import org.springframework.stereotype.Service;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.RecommendedLoanProduct;
import finpik.loanproduct.entity.RelatedLoanProduct;
import finpik.loanproduct.repository.LoanProductCacheRepository;
import finpik.loanproduct.repository.LoanProductRepository;
import finpik.loanproduct.repository.RelatedLoanProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanProductServiceImpl implements LoanProductService {
    private final LoanProductRepository loanProductRepository;
    private final LoanProductCacheRepository loanProductCacheRepository;
    private final RelatedLoanProductRepository relatedLoanProductRepository;

    @Override
    public LoanProduct getLoanProduct(Long id) {
        return findLoanProduct(id);
    }

    @Override
    public List<RecommendedLoanProduct> getRecommendedLoanProducts(Long profileId) {
        return loanProductCacheRepository.getRecommendations(profileId);
    }

    @Override
    public List<RelatedLoanProduct> getRelatedLoanProductList(Long productId) {
        return relatedLoanProductRepository.getRelatedLoanProducts(productId);
    }

    private LoanProduct findLoanProduct(Long id) {
        return loanProductRepository.findByIdWithDescription(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOAN_PRODUCT));
    }
}
