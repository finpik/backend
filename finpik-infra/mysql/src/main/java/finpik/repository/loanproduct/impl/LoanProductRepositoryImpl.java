package finpik.repository.loanproduct.impl;

import java.util.Optional;

import finpik.jpa.repository.loanproduct.LoanProductJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.entity.loanproduct.LoanProductEntity;
import finpik.loanproduct.LoanProduct;
import finpik.repository.loanproduct.LoanProductRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoanProductRepositoryImpl implements LoanProductRepository {
    private final LoanProductJpaRepository loanProductJpaRepository;

    @Override
    @Transactional
    public Optional<LoanProduct> findByIdWithDescription(Long loanProductId) {
        Optional<LoanProductEntity> loanProductEntity = loanProductJpaRepository.findByIdWithDescription(loanProductId);

        return loanProductEntity.map(LoanProductEntity::toDomain);
    }
}
