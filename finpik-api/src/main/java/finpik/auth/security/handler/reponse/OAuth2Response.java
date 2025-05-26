package finpik.auth.security.handler.reponse;

import lombok.Builder;

@Builder
public record OAuth2Response(String provider, String id) {
}
