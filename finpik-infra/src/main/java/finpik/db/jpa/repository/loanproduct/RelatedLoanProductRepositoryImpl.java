package finpik.db.jpa.repository.loanproduct;

import java.util.List;

import org.springframework.stereotype.Repository;

import finpik.db.entity.loanproduct.LoanProductEntity;
import finpik.db.jpa.repository.history.UserProductViewHistoryJpaRepository;
import finpik.db.jpa.repository.history.dto.RelatedLoanProductProjection;
import finpik.loanproduct.entity.RelatedLoanProduct;
import finpik.loanproduct.repository.RelatedLoanProductRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RelatedLoanProductRepositoryImpl implements RelatedLoanProductRepository {
    private final UserProductViewHistoryJpaRepository viewHistoryJpaRepository;
    private final LoanProductJpaRepository loanProductJpaRepository;

    @Override
    public List<RelatedLoanProduct> getRelatedLoanProducts(Long productId) {
        List<RelatedLoanProductProjection> projectionList = viewHistoryJpaRepository
                .getRelatedLoanProductList(productId);

        List<Long> relatedProductIdList = projectionList.stream().map(RelatedLoanProductProjection::getProductId)
                .toList();

        List<LoanProductEntity> entityList = loanProductJpaRepository.findAllById(relatedProductIdList);

        return entityList.stream()
                .map(it -> RelatedLoanProduct.builder().productId(it.getId()).productName(it.getProductName())
                        .maxInterestRate(it.getMaxInterestRate()).minInterestRate(it.getMinInterestRate())
                        .loanLimitAmount(it.getLoanLimitAmount()).build())
                .toList();
    }
}
