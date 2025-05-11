package com.loanpick.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.loanpick.user.entity.User;

public interface SseEmitterService {
    SseEmitter createSseEmitter(Long userId);

    void notifyRecommendationCompleted(User user);
}
