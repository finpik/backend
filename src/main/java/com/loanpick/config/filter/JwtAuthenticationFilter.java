package com.loanpick.config.filter;

import static com.loanpick.util.Values.BEGIN_INDEX;
import static com.loanpick.util.Values.REFRESH_TOKEN;
import static com.loanpick.util.Values.USER;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.loanpick.auth.oauth.constants.HttpHeaderValues;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtProvider.isValid(token)) {
            long userId = jwtProvider.getUserId(token);
            User user = userRepository.findById(userId).orElseThrow();

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
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaderValues.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(HttpHeaderValues.BEARER)) {
            return bearer.substring(BEGIN_INDEX);
        }
        return null;
    }
}
