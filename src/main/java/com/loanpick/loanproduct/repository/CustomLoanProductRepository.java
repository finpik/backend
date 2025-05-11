package com.loanpick.loanproduct.repository;

import java.util.Optional;

import com.loanpick.loanproduct.entity.LoanProduct;

public interface CustomLoanProductRepository {
    Optional<LoanProduct> findByIdWithDescription(Long loanProductId);
}
