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
    ) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        User user = userRepository.findByEmail(email).orElseGet(() ->
            userRepository.save(User.builder().username("").email(email).gender(Gender.MALE).registrationType(null).build())
        );

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600_000); // 1시간
        // JWT 발급
        String jwt = jwtProvider.createToken(user, now, expiry);

        // JSON 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
    }
}
