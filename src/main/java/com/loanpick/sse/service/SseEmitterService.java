package com.loanpick.sse.service;

import com.loanpick.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {
    SseEmitter createSseEmitter(Long userId);

    void notifyRecommendationCompleted(User user);
}
