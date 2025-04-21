package com.loanpick.auth.oauth;

import com.loanpick.auth.oauth.service.dto.CustomOAuth2User;
import com.loanpick.auth.oauth.jwt.JwtProvider;
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
    private static final int FIVE_MINUTE = 300_000;

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

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            responseSuccessLogin(response, getJwtToken(user.get()));
        } else {
            responseSignUpUser(response);
        }
    }

    private String getJwtToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + FIVE_MINUTE);
        return jwtProvider.createToken(user, now, expiry);
    }

    private void responseSuccessLogin(HttpServletResponse response, String jwt) {
        // JSON 응답
        response.setHeader(HttpHeaderValues.AUTHORIZATION, HttpHeaderValues.BEARER + jwt);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void responseSignUpUser(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType(HttpHeaderValues.APPLICATION_JSON);
        response.setCharacterEncoding(HttpHeaderValues.UTF_8);
        response.getWriter().write(ErrorMessage.SIGN_UP_NEEDED);
    }
}
