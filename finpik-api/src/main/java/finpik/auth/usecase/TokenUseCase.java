package finpik.auth.usecase;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finpik.JwtProvider;
import finpik.auth.service.AuthService;
import finpik.auth.usecase.dto.TokenRefreshResultDto;
import finpik.dto.CreateTokenDto;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.user.entity.User;
import finpik.user.service.UserService;
import finpik.util.Values;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenUseCase {
    private final AuthService authService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenRefreshResultDto refresh(String refreshToken) {
        long userId = jwtProvider.getUserId(refreshToken);
        validateRefreshToken(userId, refreshToken);

        User user = userService.findUserBy(userId);

        CreateTokenDto dto = createTokenDto(user.getId(), user.getEmail());

        String newRefreshToken = jwtProvider.createRefreshToken(dto);
        // TODO(여기 부분을 Kafka 이벤트로 발생하는걸 생각해봐야함)
        authService.saveRefreshToken(userId, newRefreshToken);

        String newAccessToken = jwtProvider.createAccessToken(dto);

        return new TokenRefreshResultDto(newRefreshToken, newAccessToken);
    }

    private void validateRefreshToken(Long userId, String refreshToken) {
        if (!authService.isValid(userId, refreshToken) && !jwtProvider.isValid(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private CreateTokenDto createTokenDto(Long userId, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Values.FOURTEEN_DAYS_MILL);

        return CreateTokenDto.builder().userId(userId).email(email).issuedAt(now).expiration(expiration).build();
    }
}
