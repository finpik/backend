package finpik.db.jpa.repository.loanproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import finpik.db.entity.loanproduct.LoanProductEntity;

public interface LoanProductJpaRepository extends JpaRepository<LoanProductEntity, Long>, CustomLoanProductRepository {
}
