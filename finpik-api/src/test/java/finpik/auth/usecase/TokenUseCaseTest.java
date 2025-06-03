package finpik.auth.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import finpik.JwtProvider;
import finpik.auth.service.AuthService;
import finpik.auth.usecase.dto.TokenRefreshResultDto;
import finpik.error.enums.ErrorCode;
import finpik.user.entity.User;
import finpik.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class TokenUseCaseTest {
    @InjectMocks
    private TokenUseCase tokenUseCase;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @DisplayName("RefreshToken을 통해 AccessToken과 RefreshToken을 새로 발급한다.")
    @Test
    void refresh() {
        // given
        String previousRefreshToken = "previousRefreshToken";
        String newRefreshToken = "newRefreshToken";
        String newAccessToken = "newAccessToken";

        User user = User.builder().id(1L).build();
        when(jwtProvider.getUserId(previousRefreshToken)).thenReturn(user.getId());
        when(userService.findUserBy(user.getId())).thenReturn(user);
        when(authService.isValid(user.getId(), previousRefreshToken)).thenReturn(true);
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
