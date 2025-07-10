package finpik.repository.loanproduct;

import finpik.RecommendedLoanProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedLoanProductRepository {
    List<RecommendedLoanProduct> saveAll(Long profileId, List<RecommendedLoanProduct> recommendedLoanProductList);

    List<RecommendedLoanProduct> findAllByProfileId(Long profileId);
}
