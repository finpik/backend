package finpik.resolver.loanproduct.application.impl;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.loanproduct.LoanProduct;
import finpik.loanproduct.RecommendedLoanProduct;
import finpik.loanproduct.RelatedLoanProduct;
import finpik.repository.loanproduct.RecommendedLoanProductCacheRepository;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.repository.loanproduct.RecommendedLoanProductRepository;
import finpik.repository.loanproduct.RelatedLoanProductRepository;
import finpik.resolver.loanproduct.application.GetLoanProductUseCase;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLoanProductUseCaseImpl implements GetLoanProductUseCase {
    private final LoanProductRepository loanProductRepository;
    private final RelatedLoanProductRepository relatedLoanProductRepository;
    private final RecommendedLoanProductCacheRepository recommendedLoanProductCacheRepository;
    private final RecommendedLoanProductRepository recommendedLoanProductRepository;

    @Transactional(readOnly = true)
    public List<RecommendedLoanProductDto> getRecommendedLoanProducts(Long profileId) {
        List<RecommendedLoanProduct> recommendedLoanProducts = recommendedLoanProductCacheRepository.findAllById(profileId);

        List<RecommendedLoanProduct> fromDBIfNotExistInRedis =
            findFromDBIfNotExistInRedis(profileId, recommendedLoanProducts);

        recommendedLoanProductCacheRepository.cacheAsync(profileId, fromDBIfNotExistInRedis);

        return fromDBIfNotExistInRedis.stream().map(RecommendedLoanProductDto::new).toList();
    }

    @Transactional(readOnly = true)
    public LoanProductDto getLoanProduct(Long loanProductId) {
        LoanProduct loanProduct = loanProductRepository.findByIdWithDescription(loanProductId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOAN_PRODUCT));

        return new LoanProductDto(loanProduct);
    }

    @Transactional(readOnly = true)
    public List<RelatedLoanProductDto> getRelatedLoanProductList(Long loanProductId) {
        List<RelatedLoanProduct> relatedLoanProductList = relatedLoanProductRepository.findAllById(loanProductId);

        return relatedLoanProductList.stream().map(RelatedLoanProductDto::new).toList();
    }

    private List<RecommendedLoanProduct> findFromDBIfNotExistInRedis(
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    ) {
        if (!recommendedLoanProductList.isEmpty()) return recommendedLoanProductList;

        return recommendedLoanProductRepository.findAllByProfileId(profileId);
    }
}
