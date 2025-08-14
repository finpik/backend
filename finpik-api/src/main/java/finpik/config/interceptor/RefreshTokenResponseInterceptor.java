package finpik.config.interceptor;

import static finpik.util.Values.FOURTEEN_DAYS_SEC;
import static finpik.util.Values.REFRESH_TOKEN;

import finpik.repository.auth.AuthCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenResponseInterceptor implements WebGraphQlInterceptor {
    private final AuthCacheRepository authCacheRepository;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request)
            .doOnNext(response -> {
                    Optional<String> refreshTokenOpt = getRefreshTokenFromContext();
                    Optional<Long> userIdOpt = getUserIdFromContext();

                    if (refreshTokenOpt.isPresent() && userIdOpt.isPresent()) {
                        String refreshToken = refreshTokenOpt.get();
                        Long userId = userIdOpt.get();

                        saveRefreshTokenCache(userId, refreshToken);
                        addRefreshTokenCookie(refreshToken);
                    }
                }
            );
    }

    private Optional<String> getRefreshTokenFromContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) return Optional.empty();

        Object token = attributes.getAttribute(REFRESH_TOKEN, RequestAttributes.SCOPE_REQUEST);
        return (token instanceof String strToken) ? Optional.of(strToken) : Optional.empty();
    }

    private Optional<Long> getUserIdFromContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) return Optional.empty();

        Object userId = attributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return (userId instanceof Long longUserId) ? Optional.of(longUserId) : Optional.empty();
    }

    private void addRefreshTokenCookie(String refreshToken) {
        HttpServletResponse response = getCurrentHttpServletResponse();
        if (response == null) return;

        createRefreshTokenCookie(response, refreshToken);
    }

    private HttpServletResponse getCurrentHttpServletResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            return servletAttributes.getResponse();
        }
        return null;
    }

    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .domain("localhost")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshTokenCache(Long userId, String refreshToken) {
        authCacheRepository.saveRefreshToken(userId, refreshToken, Duration.ofSeconds(FOURTEEN_DAYS_SEC));
    }
}
