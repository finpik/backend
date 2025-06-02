package finpik.auth.security.oauth.kakao;

public interface OAuth2UserInfo {
    String getProviderId();

    String getEmail();

    String getUserId();
}
