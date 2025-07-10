package finpik.sse.service;

import static finpik.util.Values.ONE_MINUTE_MILL;

import java.util.List;
import java.util.UUID;

import finpik.RecommendedLoanProduct;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.sse.service.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {
    private final SseEmitterRepository sseEmitterRepository;

    @Override
    public SseEmitter createSseEmitter(Long profileId) {
        SseEmitter emitter = new SseEmitter(ONE_MINUTE_MILL);

        return sseEmitterRepository.save(profileId, emitter);
    }

    @Async
    public void notifyRecommendationCompleted(
        UUID eventId,
        Long profileId,
        List<RecommendedLoanProduct> recommendedLoanProductList
    ) {
        log.info("Notify recommendation completed for {}", eventId);

        SseEmitter emitter = sseEmitterRepository.get(profileId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("recommendation")
                    .data(recommendedLoanProductList, MediaType.APPLICATION_JSON));
                emitter.complete();

            } catch (Exception e) {
                emitter.completeWithError(e);
                sseEmitterRepository.delete(profileId);
            }
        }
    }
}
