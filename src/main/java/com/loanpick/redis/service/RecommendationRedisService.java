package com.loanpick.redis.service;

import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.loanpick.redis.service.dto.RecommendedLoanProductDto;
import com.loanpick.redis.service.dto.RecommendedLoanProductDtoList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationRedisService {
    private static final String RECOMMENDATION_KEY = "recommendation";

    @CachePut(value = RECOMMENDATION_KEY, key = "#profileId")
    public RecommendedLoanProductDtoList cacheRecommendation(Long profileId,
            List<RecommendedLoanProductDto> recommendations) {
        return new RecommendedLoanProductDtoList(recommendations);
    }

    @Cacheable(value = RECOMMENDATION_KEY, key = "#profileId")
    public RecommendedLoanProductDtoList getRecommendations(Long profileId) {
        return new RecommendedLoanProductDtoList(Collections.emptyList());
    }
}
