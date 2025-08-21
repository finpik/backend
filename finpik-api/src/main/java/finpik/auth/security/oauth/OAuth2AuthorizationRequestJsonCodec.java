package finpik.auth.security.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO(성공시 리팩토링 필요)
public final class OAuth2AuthorizationRequestJsonCodec {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private OAuth2AuthorizationRequestJsonCodec() {}

    public static String encode(OAuth2AuthorizationRequest req) {
        try {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("authorizationUri", req.getAuthorizationUri());
            m.put("clientId", req.getClientId());
            m.put("redirectUri", req.getRedirectUri());
            m.put("scopes", new ArrayList<>(req.getScopes()));
            m.put("state", req.getState());
            m.put("additionalParameters", filterStringMap(req.getAdditionalParameters()));
            m.put("attributes", filterStringMap(req.getAttributes()));
            byte[] json = MAPPER.writeValueAsBytes(m);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encode OAuth2AuthorizationRequest", e);
        }
    }

    public static OAuth2AuthorizationRequest decode(String base64) {
        try {
            byte[] json = Base64.getUrlDecoder().decode(base64);
            Map<String, Object> m = MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
            String authorizationUri = (String) m.get("authorizationUri");
            String clientId = (String) m.get("clientId");
            String redirectUri = (String) m.get("redirectUri");
            String state = (String) m.get("state");

            @SuppressWarnings("unchecked")
            List<String> scopesList = (List<String>) m.getOrDefault("scopes", Collections.emptyList());
            Set<String> scopes = new LinkedHashSet<>(scopesList);

            @SuppressWarnings("unchecked")
            Map<String, String> additional = (Map<String, String>) m.getOrDefault("additionalParameters", Collections.emptyMap());
            @SuppressWarnings("unchecked")
            Map<String, String> attrs = (Map<String, String>) m.getOrDefault("attributes", Collections.emptyMap());

            OAuth2AuthorizationRequest.Builder b = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri(authorizationUri)
                .clientId(clientId)
                .redirectUri(redirectUri)
                .scopes(scopes)
                .state(state)
                .additionalParameters(new LinkedHashMap<>(additional));
            b.attributes(a -> a.putAll(attrs));

            return b.build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decode OAuth2AuthorizationRequest", e);
        }
    }

    private static Map<String, String> filterStringMap(Map<String, Object> in) {
        if (in == null) return Collections.emptyMap();
        Map<String, String> out = new LinkedHashMap<>();
        in.forEach((k, v) -> {
            if (v != null) out.put(k, String.valueOf(v));
        });
        return out;
    }
}
