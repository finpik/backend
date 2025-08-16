package finpik.repository.loanproduct;

import java.util.List;
import java.util.Optional;

import finpik.LoanProduct;
import finpik.RecommendedLoanProduct;
import finpik.RelatedLoanProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanProductRepository {
    Optional<LoanProduct> findByIdWithDescription(Long loanProductId);

    List<RecommendedLoanProduct> saveAllRecommendedLoanProduct(Long profileId, List<RecommendedLoanProduct> recommendedLoanProductList);

    Slice<RecommendedLoanProduct> findAllRecommendedLoanProductSliceByProfileId(Long profileId, Pageable pageable);

    List<RelatedLoanProduct> findAllRelatedLoanProductById(Long loanProductId);

    List<LoanProduct> updateAll(List<LoanProduct> loanProductList);

    List<LoanProduct> findAllById(List<Long> ids);
}
