package com.loanpick.loanproduct.service;

import com.loanpick.loanproduct.service.dto.RecommendLoanProductProfileDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface RecommendLoanProductService {
    @Async
    void recommendLoanProductAsync(RecommendLoanProductProfileDto dto);
}
