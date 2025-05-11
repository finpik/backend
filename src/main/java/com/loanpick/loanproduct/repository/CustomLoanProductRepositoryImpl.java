package com.loanpick.loanproduct.repository;

import static com.loanpick.loanproduct.entity.QLoanProduct.loanProduct;
import static com.loanpick.loanproduct.entity.QLoanProductDescription.loanProductDescription;

import java.util.Optional;

import com.loanpick.loanproduct.entity.LoanProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoanProductRepositoryImpl implements CustomLoanProductRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LoanProduct> findByIdWithDescription(Long loanProductId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(loanProduct).leftJoin(loanProduct.description, loanProductDescription)
                        .fetchJoin().where(loanProduct.id.eq(loanProductId)).fetchOne());
    }
}
