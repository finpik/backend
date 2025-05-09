package com.loanpick.redis.service;

import com.loanpick.redis.service.dto.RecommendedLoanProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationRedisService {
    private static final String RECOMMENDATION_KEY = "recommendation";

    @CachePut(value = RECOMMENDATION_KEY, key = "#profileId")
    public List<RecommendedLoanProductDto> cacheRecommendation(
        Long profileId,
        List<RecommendedLoanProductDto> recommendations
    ) {
        return recommendations;
    }
    
    
}
