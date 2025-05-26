package finpik.sse.service;

import static finpik.util.Values.ONE_MINUTE_MILL;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.dto.RecommendedCompleteEvent;
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
    @EventListener
    public void notifyRecommendationCompleted(RecommendedCompleteEvent event) {
        log.info("Notify recommendation completed for {}", event.eventId());

        SseEmitter emitter = sseEmitterRepository.get(event.profileId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("recommendation").data("추천 상품을 조회할 수 있습니다."));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
                sseEmitterRepository.delete(event.profileId());
            }
        }
    }
}
