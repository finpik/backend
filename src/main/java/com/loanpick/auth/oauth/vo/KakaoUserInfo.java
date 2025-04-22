package com.loanpick.auth.oauth.vo;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {
  private final Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getEmail() {
    ObjectMapper mapper = new ObjectMapper();

    Map<String, Object> account =
        mapper.convertValue(attributes.get("kakao_account"), new TypeReference<>() {});
    return (String) account.get("email");
  }

  @Override
  public String getUserId() {
    return attributes.get("id").toString();
  }
}
