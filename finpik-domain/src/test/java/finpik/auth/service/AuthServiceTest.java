package finpik.auth.service;

import static finpik.util.Values.FOURTEEN_DAYS_MILL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.auth.repository.AuthCacheRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthCacheRepository authCacheRepository;

    @DisplayName("리프레시 토큰이 유효하다면 true를 리턴한다.")
    @Test
    void isValid() {
        // given
        Long userId = 1L;
        String refreshToken = "testRefreshToken";
        when(authCacheRepository.isRefreshTokenValid(userId, refreshToken)).thenReturn(true);

        // when
        boolean valid = authService.isValid(userId, refreshToken);

        // then
        Assertions.assertTrue(valid);
    }

    @DisplayName("리프레시 토큰을 저장하면 saveRefreshToken이 호출되고 파라미터가 일치한다.")
    @Test
    void saveRefreshToken() {
        // given
        Long userId = 1L;
        String refreshToken = "testRefreshToken";
        Duration duration = Duration.ofMillis(FOURTEEN_DAYS_MILL);

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> refreshTokenCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        // when
        authService.saveRefreshToken(userId, refreshToken);

        // then
        verify(authCacheRepository).saveRefreshToken(userIdCaptor.capture(), refreshTokenCaptor.capture(),
                durationCaptor.capture());

        assertAll(() -> assertThat(userId).isEqualTo(userIdCaptor.getValue()),
                () -> assertThat(refreshToken).isEqualTo(refreshTokenCaptor.getValue()),
                () -> assertThat(duration).isEqualTo(durationCaptor.getValue()));
    }
}
