package finpik.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.dto.RecommendedCompleteEvent;

public interface SseEmitterService {
    SseEmitter createSseEmitter(Long profileId);

    void notifyRecommendationCompleted(RecommendedCompleteEvent event);
}
