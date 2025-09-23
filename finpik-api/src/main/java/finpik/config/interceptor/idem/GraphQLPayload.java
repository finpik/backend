package finpik.config.interceptor.idem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public record GraphQLPayload(
        String operationName,
        String query,
        Map<String, Object> variables
) {
    private static final ObjectMapper om = new ObjectMapper();

    static String buildGraphQLIdemKey(String userId, GraphQLPayload p) {
        String opName = (p.operationName() == null || p.operationName().isBlank()) ? "anonymous" : p.operationName();

        String opType = GraphQLUtils.detectOperationType(p.query(), p.operationName()); // "mutation" or "query"
        // mutation일 때만 사용할지 여기서 결정해도 됨

        String normalizedQuery = GraphQLUtils.normalizeQuery(p.query()); // 공백/주석 제거
        String canonicalVars   = FingerprintUtils.canonicalize(serializeJson(p.variables())); // 키 정렬 JSON

        String qh = FingerprintUtils.sha256UrlBase64(normalizedQuery);
        String vh = FingerprintUtils.sha256UrlBase64(canonicalVars);

        return "%s|GQL|%s|%s|Q:%s|V:%s".formatted(userId, opType, opName, qh, vh);
    }

    public static String serializeJson(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}
