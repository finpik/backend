package com.loanpick.auth.oauth.vo;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpick.constant.ConstantValue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return attributes.get(ConstantValue.USER_ID_FROM_VENDOR).toString();
    }

    @Override
    public String getEmail() {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> account = mapper.convertValue(attributes.get(ConstantValue.KAKAO_ACCOUNT),
                new TypeReference<>() {
                });
        return (String) account.get(ConstantValue.EMAIL);
    }

    @Override
    public String getUserId() {
        return attributes.get(ConstantValue.USER_ID_FROM_VENDOR).toString();
    }
}
