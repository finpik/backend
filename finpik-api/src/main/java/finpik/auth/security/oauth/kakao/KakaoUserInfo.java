package finpik.auth.security.oauth.kakao;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import static finpik.util.Values.EMAIL;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String USER_ID_FROM_VENDOR = "id";

    @Override
    public String getProviderId() {
        return attributes.get(USER_ID_FROM_VENDOR).toString();
    }

    @Override
    public String getEmail() {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> account = mapper.convertValue(attributes.get(KAKAO_ACCOUNT), new TypeReference<>() {
        });
        return (String) account.get(EMAIL);
    }

    @Override
    public String getUserId() {
        return attributes.get(USER_ID_FROM_VENDOR).toString();
    }
}
