package finpik.repository.auth;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

/*
 * TODO() Access Token Refactoring
 * Access Token 리팩토링 필요
 * 검증 후 맞으면 레디스에서 지워주는 형태로 재설정 필요
 * 관련 검증 프로세스를 묶어 추상화 시킨 후 하나의 함수로 제공하는 형태가 좋을듯
 */
@Repository
@RequiredArgsConstructor
public class AuthRedisRepositoryImpl implements AuthRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String AUTHENTICATION_PREFIX = "authentication:";
    private static final String REFRESH_TOKEN = "refresh_token:";
    private static final String ACCESS_TOKEN = "access_token:";
    private static final String COLON = ":";

    @Override
    public void saveEmailForSignUp(String id, String provider, String accessToken, Duration ttl) {
        String customKey = getAccessTokenKey(id, provider);
        redisTemplate.opsForValue().set(customKey, accessToken, ttl);
    }

    @Override
    public String getEmailByCustomId(String id, String provider) {
        String customKey = getAccessTokenKey(id, provider);
        return redisTemplate.opsForValue().get(customKey);
    }

    @Override
    public void saveRefreshToken(Long userId, String refreshToken, Duration ttl) {
        String key = getRefreshTokenKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, ttl);
    }

    @Override
    public boolean isRefreshTokenValid(Long userId, String refreshToken) {
        String key = getRefreshTokenKey(userId);
        String saved = redisTemplate.opsForValue().get(key);
        return saved != null && saved.equals(refreshToken);
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(getRefreshTokenKey(userId));
    }

    private String getAccessTokenKey(String id, String provider) {
        return AUTHENTICATION_PREFIX + ACCESS_TOKEN + id + COLON + provider;
    }

    private String getRefreshTokenKey(Long userId) {
        return AUTHENTICATION_PREFIX + REFRESH_TOKEN + userId;
    }
}
