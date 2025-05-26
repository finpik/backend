package finpik.auth.repository;

import java.time.Duration;

public interface AuthCacheRepository {
    boolean isRefreshTokenValid(Long userId, String refreshToken);

    void saveRefreshToken(Long userId, String refreshToken, Duration ttl);

    String getEmailByCustomId(String id, String provider);
}
