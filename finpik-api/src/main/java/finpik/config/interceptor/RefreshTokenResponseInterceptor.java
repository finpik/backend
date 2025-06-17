package finpik.config.interceptor;

import static finpik.util.Values.FOURTEEN_DAYS_SEC;
import static finpik.util.Values.GRAPHQL_URL;
import static finpik.util.Values.REFRESH_TOKEN;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class RefreshTokenResponseInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request)
            .doOnNext(response -> getRefreshTokenFromContext()
                .ifPresent(this::addRefreshTokenCookie));
    }

    private Optional<String> getRefreshTokenFromContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) return Optional.empty();

        Object token = attributes.getAttribute(REFRESH_TOKEN, RequestAttributes.SCOPE_REQUEST);
        return (token instanceof String strToken) ? Optional.of(strToken) : Optional.empty();
    }

    private void addRefreshTokenCookie(String refreshToken) {
        HttpServletResponse response = getCurrentHttpServletResponse();
        if (response == null) return;

        Cookie cookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);
    }

    private HttpServletResponse getCurrentHttpServletResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            return servletAttributes.getResponse();
        }
        return null;
    }

    private Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(GRAPHQL_URL);
        cookie.setMaxAge(FOURTEEN_DAYS_SEC);
        return cookie;
    }
}
