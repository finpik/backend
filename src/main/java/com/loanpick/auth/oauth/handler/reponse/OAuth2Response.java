package com.loanpick.auth.oauth.handler.reponse;

import lombok.Builder;

@Builder
public record OAuth2Response(String provider, String id) {
}
