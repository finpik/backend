package finpik.resolver.loanproduct.application.impl;

import finpik.LoanProduct;
import finpik.RecommendedLoanProduct;
import finpik.RelatedLoanProduct;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.resolver.loanproduct.application.GetLoanProductUseCase;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLoanProductUseCaseImpl implements GetLoanProductUseCase {
    private final LoanProductRepository loanProductRepository;

    @Transactional(readOnly = true)
    public Slice<RecommendedLoanProductDto> getRecommendedLoanProductList(Long profileId, Pageable pageable) {
        Slice<RecommendedLoanProduct> recommendedLoanProductList =
            loanProductRepository.findAllRecommendedLoanProductSliceByProfileId(profileId, pageable);

        return new SliceImpl<>(
            recommendedLoanProductList.stream().map(RecommendedLoanProductDto::new).toList(),
            pageable,
            recommendedLoanProductList.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public LoanProductDto getLoanProduct(Long loanProductId, Long profileId) {
        LoanProduct loanProduct = loanProductRepository.findByIdWithDescription(loanProductId, profileId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOAN_PRODUCT));

        return new LoanProductDto(loanProduct);
    }

    @Transactional(readOnly = true)
    public List<RelatedLoanProductDto> getRelatedLoanProductList(Long loanProductId) {
        List<RelatedLoanProduct> relatedLoanProductList = loanProductRepository.findAllRelatedLoanProductById(loanProductId);

        return relatedLoanProductList.stream().map(RelatedLoanProductDto::new).toList();
    }
}
