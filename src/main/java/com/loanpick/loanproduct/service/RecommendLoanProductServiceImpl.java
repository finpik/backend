package com.loanpick.loanproduct.service;

import com.loanpick.externalapi.recommend.RecommendLoanProductPort;
import com.loanpick.externalapi.recommend.result.RecommendLoanProductResult;
import com.loanpick.loanproduct.service.dto.RecommendLoanProductProfileDto;
import com.loanpick.redis.service.RecommendationRedisService;
import com.loanpick.redis.service.dto.RecommendedLoanProductDto;
import com.loanpick.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Async
public class RecommendLoanProductServiceImpl implements RecommendLoanProductService {
    private final RecommendLoanProductPort recommendLoanProductPort;
    private final RecommendationRedisService recommendationRedisService;
    private final SseEmitterService sseEmitterService;

    @Override
    public void recommendLoanProductAsync(RecommendLoanProductProfileDto dto) {
        List<RecommendLoanProductResult> recommendations =
            recommendLoanProductPort.getRecommendations(dto.toRequest());

        List<RecommendedLoanProductDto> dtos =
            recommendations.stream().map(RecommendLoanProductResult::toDto).toList();

        recommendationRedisService.cacheRecommendation(dto.getProfileId(), dtos);

        sseEmitterService.notifyRecommendationCompleted(dto.getUser());
    }
}
