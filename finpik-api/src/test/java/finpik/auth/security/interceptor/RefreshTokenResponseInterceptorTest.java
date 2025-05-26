package finpik.auth.security.interceptor;

import static finpik.util.Values.GRAPHQL_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.ExecutionGraphQlResponse;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import jakarta.servlet.http.Cookie;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class RefreshTokenResponseInterceptorTest {

    private final RefreshTokenResponseInterceptor interceptor = new RefreshTokenResponseInterceptor();
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String TEST_REFRESH_TOKEN = "testRefreshToken";
    private static final String QUERY = "query {test}";

    @AfterEach
    public void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @DisplayName("refreshToken이 존재하면 쿠키가 응답에 추가된다")
    @Test
    void addsCookieWhenRefreshTokenExists() {
        // given
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest(),
                servletResponse);
        attributes.setAttribute(REFRESH_TOKEN, TEST_REFRESH_TOKEN, RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.setRequestAttributes(attributes);

        WebGraphQlRequest mockRequest = mock(WebGraphQlRequest.class);
        ExecutionInput input = ExecutionInput.newExecutionInput().query(QUERY).build();
        ExecutionResult result = ExecutionResultImpl.newExecutionResult().data(Map.of()).build();
        ExecutionGraphQlResponse execResponse = new DefaultExecutionGraphQlResponse(input, result);
        WebGraphQlResponse webResponse = new WebGraphQlResponse(execResponse);

        WebGraphQlInterceptor.Chain mockChain = req -> Mono.just(webResponse);

        // when
        interceptor.intercept(mockRequest, mockChain).block();

        // then
        Cookie[] cookies = servletResponse.getCookies();
        assertThat(cookies).isNotNull();
        assertThat(cookies)
                .anyMatch(cookie -> REFRESH_TOKEN.equals(cookie.getName()) && REFRESH_TOKEN.equals(cookie.getValue())
                        && cookie.isHttpOnly() && cookie.getSecure() && GRAPHQL_URL.equals(cookie.getPath()));
    }

    @DisplayName("refreshToken이 없으면 쿠키가 추가되지 않는다")
    @Test
    void doesNothingIfRefreshTokenIsAbsent() {
        // given
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest(),
                servletResponse);
        RequestContextHolder.setRequestAttributes(attributes); // refreshToken 없음

        WebGraphQlRequest mockRequest = mock(WebGraphQlRequest.class);
        ExecutionInput input = ExecutionInput.newExecutionInput().query(QUERY).build();
        ExecutionResult result = ExecutionResultImpl.newExecutionResult().data(Map.of()).build();
        ExecutionGraphQlResponse execResponse = new DefaultExecutionGraphQlResponse(input, result);
        WebGraphQlResponse webResponse = new WebGraphQlResponse(execResponse);

        WebGraphQlInterceptor.Chain mockChain = req -> Mono.just(webResponse);

        // when
        interceptor.intercept(mockRequest, mockChain).block();

        // then
        assertThat(servletResponse.getCookies()).isEmpty();
    }
}
