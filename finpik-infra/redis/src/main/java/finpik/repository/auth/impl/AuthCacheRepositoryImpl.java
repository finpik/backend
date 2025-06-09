package finpik.repository.auth.impl;

import java.time.Duration;

import finpik.repository.auth.AuthCacheRepository;
import finpik.repository.auth.AuthRedisRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthCacheRepositoryImpl implements AuthCacheRepository {
    private final AuthRedisRepository authRedisRepository;

    @Override
    public boolean isRefreshTokenValid(Long userId, String refreshToken) {
        return authRedisRepository.isRefreshTokenValid(userId, refreshToken);
    }

    @Override
    public void saveRefreshToken(Long userId, String refreshToken, Duration ttl) {
        authRedisRepository.saveRefreshToken(userId, refreshToken, ttl);
    }

    @Override
    public String getEmailByCustomId(String id, String provider) {
        return authRedisRepository.getEmailByCustomId(id, provider);
    }
}
