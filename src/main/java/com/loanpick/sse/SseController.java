package com.loanpick.sse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.loanpick.sse.service.SseEmitterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@CrossOrigin(origins = "*") // 또는 "*"
public class SseController {
    private final SseEmitterService sseEmitterService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = sseEmitterService.createSseEmitter(userId);

        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE 연결됨"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
