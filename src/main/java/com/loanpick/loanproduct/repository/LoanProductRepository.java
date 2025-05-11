package com.loanpick.loanproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loanpick.loanproduct.entity.LoanProduct;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Long>, CustomLoanProductRepository {
}
