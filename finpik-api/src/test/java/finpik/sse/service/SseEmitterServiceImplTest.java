package finpik.sse.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.dto.RecommendedCompleteEvent;
import finpik.sse.service.repository.SseEmitterRepository;

@ExtendWith(MockitoExtension.class)
class SseEmitterServiceImplTest {
    @InjectMocks
    private SseEmitterServiceImpl sseEmitterService;

    @Mock
    private SseEmitterRepository sseEmitterRepository;

    @Mock
    private SseEmitter emitter;

    @DisplayName("createSseEmitter는 사용자 ID로 SseEmitter를 생성하고 저장소에 저장한다.")
    @Test
    void createSseEmitterTest() {
        // given
        Long userId = 1L;
        given(sseEmitterRepository.save(eq(userId), any(SseEmitter.class))).willReturn(emitter);

        // when
        SseEmitter result = sseEmitterService.createSseEmitter(userId);

        // then
        then(sseEmitterRepository).should().save(eq(userId), any(SseEmitter.class));
        assertThat(result).isEqualTo(emitter);
    }

    @DisplayName("notifyRecommendationCompleted는 emitter가 존재하면 알림을 보내고 complete를 호출한다.")
    @Test
    void notifyWhenEmitterExists() throws Exception {
        // given
        Long profileId = 1L;
        given(sseEmitterRepository.get(profileId)).willReturn(emitter);

        RecommendedCompleteEvent event = RecommendedCompleteEvent.of(UUID.randomUUID(), profileId, List.of());

        // when
        sseEmitterService.notifyRecommendationCompleted(event);

        // then
        then(emitter).should().send(any(SseEmitter.SseEventBuilder.class));
        then(emitter).should().complete();
    }

    @DisplayName("notifyRecommendationCompleted는 emitter가 없으면 아무것도 하지 않는다.")
    @Test
    void notifyWhenEmitterNotExists() {
        // given
        Long profileId = 1L;
        given(sseEmitterRepository.get(profileId)).willReturn(null);

        RecommendedCompleteEvent event = RecommendedCompleteEvent.of(UUID.randomUUID(), profileId, List.of());

        // when
        sseEmitterService.notifyRecommendationCompleted(event);

        // then
        then(emitter).shouldHaveNoInteractions();
    }

    @DisplayName("notifyRecommendationCompleted는 send 도중 예외가 발생하면 completeWithError 및 삭제를 호출한다.")
    @Test
    void notifyWithSendFailure() throws Exception {
        // given
        Long profileId = 1L;
        given(sseEmitterRepository.get(profileId)).willReturn(emitter);
        willThrow(new IOException("send 실패")).given(emitter).send(any(SseEmitter.SseEventBuilder.class));

        RecommendedCompleteEvent event = RecommendedCompleteEvent.of(UUID.randomUUID(), profileId, List.of());

        // when
        sseEmitterService.notifyRecommendationCompleted(event);

        // then
        then(emitter).should().completeWithError(any(IOException.class));
        then(sseEmitterRepository).should().delete(profileId);
    }
}
