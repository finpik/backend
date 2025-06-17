package finpik.auth.security.oauth.kakao;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new BusinessException(ErrorCode.UNAVAILABLE_LOGIN_WAY);
        };
    }
}
