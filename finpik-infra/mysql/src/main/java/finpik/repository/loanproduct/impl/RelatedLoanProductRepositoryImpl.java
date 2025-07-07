package finpik.repository.loanproduct.impl;

import java.util.List;

import finpik.jpa.repository.loanproduct.LoanProductJpaRepository;
import finpik.loanproduct.vo.InterestRate;
import org.springframework.stereotype.Repository;

import finpik.entity.loanproduct.LoanProductEntity;
import finpik.jpa.repository.history.userproductview.UserProductViewHistoryJpaRepository;
import finpik.jpa.repository.history.userproductview.projection.RelatedLoanProductProjection;
import finpik.loanproduct.RelatedLoanProduct;
import finpik.repository.loanproduct.RelatedLoanProductRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RelatedLoanProductRepositoryImpl implements RelatedLoanProductRepository {
    private final UserProductViewHistoryJpaRepository viewHistoryJpaRepository;
    private final LoanProductJpaRepository loanProductJpaRepository;

    @Override
    public List<RelatedLoanProduct> findAllById(Long productId) {
        List<RelatedLoanProductProjection> projectionList = viewHistoryJpaRepository
            .getRelatedLoanProductList(productId);

        List<Long> relatedProductIdList = projectionList.stream().map(RelatedLoanProductProjection::getProductId)
            .toList();

        List<LoanProductEntity> entityList = loanProductJpaRepository.findAllById(relatedProductIdList);

        return entityList.stream()
            .map(entity ->
                RelatedLoanProduct.of(
                    entity.getId(),
                    entity.getProductName(),
                    entity.getMaxInterestRate(),
                    entity.getMinInterestRate(),
                    entity.getMaxLoanLimitAmount()
                )
            ).toList();
    }
}
