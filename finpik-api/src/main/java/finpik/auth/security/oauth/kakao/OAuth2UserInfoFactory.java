package finpik.auth.security.oauth.kakao;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new RuntimeException("지원하지 않는 로그인 방식입니다. " + provider);
        };
    }
}
