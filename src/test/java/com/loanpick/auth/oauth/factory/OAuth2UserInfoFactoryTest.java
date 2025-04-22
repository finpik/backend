package com.loanpick.auth.oauth.factory;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.loanpick.auth.oauth.vo.KakaoUserInfo;
import com.loanpick.auth.oauth.vo.OAuth2UserInfo;

class OAuth2UserInfoFactoryTest {

    @DisplayName("provider (kakao)의 이름에 따라 관련된 UserInfo 객체를 리턴한다.")
    @Test
    void getKakaoOAuth2UserInfo() {
        // given
        String provider = "kakao";
        Map<String, Object> attributes = new HashMap<>();

        // when
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);

        // then
        Assertions.assertInstanceOf(KakaoUserInfo.class, oAuth2UserInfo);
    }
}
