package finpik.auth.security.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class OAuth2AuthorizationRequestCookieRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "OAUTH2_AUTH_REQ";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
        if (cookie == null) return null;
        return OAuth2AuthorizationRequestJsonCodec.decode(cookie.getValue());
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        String base64 = OAuth2AuthorizationRequestJsonCodec.encode(authorizationRequest);
        Cookie cookie = new Cookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME, base64);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_EXPIRE_SECONDS);
        response.addCookie(cookie);
        addSameSiteNone(response, cookie.getName());
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        OAuth2AuthorizationRequest existing = loadAuthorizationRequest(request);

        Cookie cookie = getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
        if (cookie != null) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);
            addSameSiteNone(response, cookie.getName());
        }
        return existing;
    }

    private static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) if (name.equals(c.getName())) return c;
        return null;
    }

    private static void addSameSiteNone(HttpServletResponse response, String cookieName) {
        for (String header : response.getHeaders("Set-Cookie")) {
            if (header.startsWith(cookieName + "=") && !header.toLowerCase().contains("samesite")) {
                response.setHeader("Set-Cookie", header + "; SameSite=None; Secure");
                break;
            }
        }
    }
}
