package finpik.config.filter;

import static finpik.util.Values.REFRESH_TOKEN;
import static finpik.util.Values.USER;

import java.io.IOException;

import finpik.JwtProvider;
import finpik.entity.User;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.repository.user.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private static final int BEGIN_INDEX = 7;
    private static final String BEARER = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //로그 시작
        long t0 = System.nanoTime();
        String token = resolveToken(request);

        if (token != null && jwtProvider.isValid(token)) {
            long userId = jwtProvider.getUserId(token);
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

            request.setAttribute(USER, user);
        }

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (REFRESH_TOKEN.equals(cookie.getName())) {
                    request.setAttribute(REFRESH_TOKEN, cookie.getValue());
                }
            }
        }

        chain.doFilter(request, response);
        //로그 종료
        long tookMs = (System.nanoTime()-t0)/1_000_000;
        log.info("[TIMING] {} {} -> {} {}ms len={}",
            request.getMethod(), request.getRequestURI(), response.getStatus(), tookMs, response.getBufferSize());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEGIN_INDEX);
        }
        return null;
    }
}
