package finpik.auth.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import finpik.entity.enums.Gender;
import finpik.entity.enums.RegistrationType;
import finpik.repository.auth.AuthCacheRepository;
import finpik.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.JwtProvider;
import finpik.auth.usecase.dto.TokenRefreshResultDto;
import finpik.error.enums.ErrorCode;
import finpik.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TokenUseCaseTest {
    @InjectMocks
    private TokenUseCase tokenUseCase;

    @Mock
    private AuthCacheRepository authCacheRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @DisplayName("RefreshToken을 통해 AccessToken과 RefreshToken을 새로 발급한다.")
    @Test
    void refresh() {
        // given
        String previousRefreshToken = "previousRefreshToken";
        String newRefreshToken = "newRefreshToken";
        String newAccessToken = "newAccessToken";

        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        when(jwtProvider.getUserId(previousRefreshToken)).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(authCacheRepository.isRefreshTokenValid(user.getId(), previousRefreshToken)).thenReturn(true);
        when(jwtProvider.createAccessToken(any())).thenReturn(newAccessToken);
        when(jwtProvider.createRefreshToken(any())).thenReturn(newRefreshToken);

        // when
        TokenRefreshResultDto result = tokenUseCase.refresh(previousRefreshToken);

        // then
        assertAll(() -> assertThat(result.newAccessToken()).isEqualTo(newAccessToken),
                () -> assertThat(result.newRefreshToken()).isEqualTo(newRefreshToken));
    }

    @DisplayName("RefreshToken이 문제가 있다면 에러가 발생한다.")
    @Test
    void wrongRefreshToken() {
        // given
        String wrongRefreshToken = "wrongRefreshToken";
        when(jwtProvider.getUserId(wrongRefreshToken)).thenReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> tokenUseCase.refresh(wrongRefreshToken))
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }
}
