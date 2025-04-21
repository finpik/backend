package com.loanpick.auth.oauth;

import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.auth.oauth.jwt.JwtProvider;
import com.loanpick.user.entity.Gender;
import com.loanpick.user.entity.User;
import com.loanpick.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + 3600_000); // 1시간
            // JWT 발급
            String jwt = jwtProvider.createToken(user.get(), now, expiry);

            // JSON 응답
            response.setHeader("Authorization", "Bearer " + jwt);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

/**
 * 로그인, 회원가입을 위한 OAuth2 인가 인증은 항상 일어나야함
 * 카카오쪽 인가 인증이 안될 경우 403
 * 유저가 없다면 -> 회원가입을 해야하는 상태라면 404
 * 이미 있는 유저라면 token을 리턴해주는 걸로 결정
 */
