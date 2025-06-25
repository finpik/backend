package finpik.jpa.repository.loanproduct;

import finpik.entity.loanproduct.RecommendedLoanProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendedLoanProductJpaRepository extends
    JpaRepository<RecommendedLoanProductEntity, UUID>,
    CustomRecommendedLoanProductJpaRepository {}
