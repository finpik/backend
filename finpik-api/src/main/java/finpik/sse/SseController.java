package finpik.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.sse.service.SseEmitterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {
    private final SseEmitterService sseEmitterService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(Long profileId, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");

        SseEmitter emitter = sseEmitterService.createSseEmitter(profileId);

        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE 연결됨"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
