package finpik.sse.service;

import finpik.loanproduct.RecommendedLoanProduct;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

public interface SseEmitterService {
    SseEmitter createSseEmitter(Long profileId);

    void notifyRecommendationCompleted(
        UUID eventId,
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    );
}
