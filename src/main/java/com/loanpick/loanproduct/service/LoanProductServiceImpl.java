package com.loanpick.loanproduct.service;

import org.springframework.stereotype.Service;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.loanproduct.entity.LoanProduct;
import com.loanpick.loanproduct.repository.LoanProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanProductServiceImpl implements LoanProductService {
    private final LoanProductRepository loanProductRepository;

    @Override
    public LoanProduct getLoanProduct(Long id) {
        return findLoanProduct(id);
    }

    private LoanProduct findLoanProduct(Long id) {
        return loanProductRepository.findByIdWithDescription(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOAN_PRODUCT));
    }
}
