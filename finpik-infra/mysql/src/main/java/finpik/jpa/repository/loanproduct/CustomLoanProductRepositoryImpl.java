package finpik.jpa.repository.loanproduct;

import static finpik.entity.loanproduct.QLoanProductDescriptionEntity.loanProductDescriptionEntity;
import static finpik.entity.loanproduct.QLoanProductEntity.loanProductEntity;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import finpik.entity.loanproduct.LoanProductEntity;
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
