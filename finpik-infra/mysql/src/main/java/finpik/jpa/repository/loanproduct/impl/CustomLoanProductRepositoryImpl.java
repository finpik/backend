package finpik.jpa.repository.loanproduct.impl;

import static finpik.entity.loanproduct.QLoanProductDescriptionEntity.loanProductDescriptionEntity;
import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.entity.loanproduct.LoanProductEntity;
import finpik.jpa.repository.loanproduct.CustomLoanProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoanProductRepositoryImpl implements CustomLoanProductRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LoanProductEntity> findByIdWithDescription(Long loanProductId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(loanProductEntity)
                .leftJoin(loanProductEntity.description, loanProductDescriptionEntity).fetchJoin()
                .where(loanProductEntity.id.eq(loanProductId)).fetchOne());
    }
}
