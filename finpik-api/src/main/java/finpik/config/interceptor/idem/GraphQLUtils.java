package finpik.config.interceptor.idem;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphQLUtils {
    public static String normalizeQuery(String q) {
        if (q == null) return "";
        // /* */ 스타일과 # 라인 주석 제거
        String noBlock = q.replaceAll("(?s)/\\*.*?\\*/", "");
        String noLine  = noBlock.replaceAll("(?m)^\\s*#.*?$", "");
        // 다중 공백/개행을 단일 공백으로
        return noLine.replaceAll("\\s+", " ").trim();
    }

    // 라이트 판별: 문서 시작부에서 operation 키워드 찾기
    public static String detectOperationType(String doc, String opName) {
        String s = normalizeQuery(doc).toLowerCase();
        if (s.startsWith("mutation") || s.contains(" mutation ")) return "mutation";
        return "query"; // subscriptions는 제외(웹소켓)
    }
}
