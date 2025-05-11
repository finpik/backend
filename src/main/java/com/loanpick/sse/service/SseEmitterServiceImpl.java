package com.loanpick.sse.service;

import static com.loanpick.util.Values.ONE_MINUTE_MILL;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.loanpick.sse.service.repository.SseEmitterRepository;
import com.loanpick.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {
    private final SseEmitterRepository sseEmitterRepository;

    @Override
    public SseEmitter createSseEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(ONE_MINUTE_MILL);

        return sseEmitterRepository.save(userId, emitter);
    }

    public void notifyRecommendationCompleted(User user) {
        SseEmitter emitter = sseEmitterRepository.get(user.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("recommendation").data("추천 상품을 조회할 수 있습니다."));
                emitter.complete(); // 완료 후 연결 끊기 (옵션)
            } catch (Exception e) {
                emitter.completeWithError(e);
                sseEmitterRepository.delete(user.getId());
            }
        }
    }
}
