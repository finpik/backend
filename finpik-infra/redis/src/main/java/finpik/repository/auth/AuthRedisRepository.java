package finpik.repository.auth;

import java.time.Duration;

public interface AuthRedisRepository {
    void saveEmailForSignUp(String id, String provider, String accessToken, Duration ttl);

    String getEmailByCustomId(String id, String provider);

    void saveRefreshToken(Long userId, String refreshToken, Duration ttl);

    boolean isRefreshTokenValid(Long userId, String refreshToken);

    void deleteRefreshToken(Long userId);
}
