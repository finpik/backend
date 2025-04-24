package com.loanpick.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationType {
    KAKAO("kakao");

    private final String name;

    public static RegistrationType fromName(String name) {
        for (RegistrationType registrationType : RegistrationType.values()) {
            if (registrationType.getName().equals(name)) {
                return registrationType;
            }
        }

        return null;
    }
}
