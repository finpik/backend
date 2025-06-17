package finpik.sse.service;

import static finpik.util.Values.ONE_MINUTE_MILL;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.dto.RecommendedCompleteEvent;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyRecommendationCompleted(RecommendedCompleteEvent event) {
        log.info("Notify recommendation completed for {}", event.eventId());

        List<RecommendedLoanProductResult> resultList = event.contentList().stream()
            .map(it -> new
                RecommendedLoanProductResult(
                it.loanProductId(), it.productName(), it.minInterestRate(),
                it.maxInterestRate(), it.loanLimitAmount())
            ).toList();

        SseEmitter emitter = sseEmitterRepository.get(event.profileId());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("recommendation")
                    .data(resultList, MediaType.APPLICATION_JSON));
                emitter.complete();

            } catch (Exception e) {
                emitter.completeWithError(e);
                sseEmitterRepository.delete(event.profileId());
            }
        }
    }
}
