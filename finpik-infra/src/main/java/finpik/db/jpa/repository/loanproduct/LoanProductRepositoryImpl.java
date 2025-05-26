package finpik.db.jpa.repository.loanproduct;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import finpik.db.entity.loanproduct.LoanProductEntity;
import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.repository.LoanProductRepository;
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
