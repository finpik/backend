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

@Component
public class RefreshTokenResponseInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request).doOnNext(response -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String newRefreshToken = (String) attributes.getAttribute(REFRESH_TOKEN,
                        RequestAttributes.SCOPE_REQUEST);
                if (newRefreshToken != null) {
                    HttpServletResponse servletResponse = ((ServletRequestAttributes) attributes).getResponse();
                    if (servletResponse != null) {
                        Cookie cookie = new Cookie(REFRESH_TOKEN, newRefreshToken);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(true);
                        cookie.setPath(GRAPHQL_URL);
                        cookie.setMaxAge(FOURTEEN_DAYS_SEC); // 14일
                        servletResponse.addCookie(cookie);
                    }
                }
            }
        });
    }
}
