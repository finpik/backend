package com.loanpick.loanproduct.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.loanpick.loanproduct.service.dto.RecommendLoanProductProfileDto;
import com.loanpick.redis.service.dto.RecommendedLoanProductDtoList;

@Service
public interface RecommendLoanProductService {
    @Async
    void recommendLoanProductAsync(RecommendLoanProductProfileDto dto);

    RecommendedLoanProductDtoList getRecommendedLoanProducts(Long profileId);
}
