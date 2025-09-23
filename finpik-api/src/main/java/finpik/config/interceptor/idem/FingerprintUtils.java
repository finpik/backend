package finpik.config.interceptor.idem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class FingerprintUtils {
    private static final ObjectMapper OM = new ObjectMapper()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

    private FingerprintUtils(){}

    @SneakyThrows
    public static String canonicalize(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) return "{}";
        var node = OM.readTree(rawJson);
        return OM.writeValueAsString(node); // 키 정렬 + 공백 제거
    }

    @SneakyThrows
    public static String sha256UrlBase64(String s) {
        var md = MessageDigest.getInstance("SHA-256");
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(md.digest(s.getBytes(StandardCharsets.UTF_8)));
    }
}
