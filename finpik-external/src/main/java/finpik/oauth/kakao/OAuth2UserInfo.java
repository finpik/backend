package finpik.oauth.kakao;

public interface OAuth2UserInfo {
    String getProviderId();

    String getEmail();

    String getUserId();
}
