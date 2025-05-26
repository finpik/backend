package finpik.sse.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class SseEmitterRepositoryTest {
    private final SseEmitterRepository repository = new SseEmitterRepository();

    private final Long userId = 1L;

    @DisplayName("SseEmitter를 저장하고 ID로 조회할 수 있다.")
    @Test
    void saveAndGetTest() {
        // given
        SseEmitter emitter = new SseEmitter();
        repository.save(userId, emitter);

        // when
        SseEmitter found = repository.get(userId);

        // then
        assertThat(found).isEqualTo(emitter);
    }

    @DisplayName("SseEmitter를 삭제하면 더 이상 조회할 수 없다.")
    @Test
    void deleteTest() {
        // given
        SseEmitter emitter = new SseEmitter();
        repository.save(userId, emitter);

        // when
        repository.delete(userId);

        // then
        assertThat(repository.get(userId)).isNull();
    }
}
