package finpik.auth.service;

import static finpik.util.Values.FOURTEEN_DAYS_MILL;

import java.time.Duration;

import org.springframework.stereotype.Service;

import finpik.auth.repository.AuthCacheRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthCacheRepository authCacheRepository;

    public boolean isValid(Long userId, String refreshToken) {
        return authCacheRepository.isRefreshTokenValid(userId, refreshToken);
    }

    public void saveRefreshToken(Long userId, String refreshToken) {
        authCacheRepository.saveRefreshToken(userId, refreshToken, Duration.ofMillis(FOURTEEN_DAYS_MILL));
    }
}
