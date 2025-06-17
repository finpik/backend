package finpik;

import static finpik.util.Values.ACCESS_TOKEN;
import static finpik.util.Values.EMAIL;
import static finpik.util.Values.REFRESH_TOKEN;
import static finpik.util.Values.TOKEN_TYPE;
import static io.jsonwebtoken.SignatureAlgorithm.*;

import java.security.Key;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import finpik.dto.CreateTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
    private final Key signingKey;

    public JwtProvider(@Value("${fin-pik.jwt.secretKey}") String base64SecretKey) {
        log.info("Initializing JWT provider = {}", base64SecretKey);
        byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(CreateTokenDto dto) {
        return generateToken(dto, ACCESS_TOKEN);
    }

    public String createRefreshToken(CreateTokenDto dto) {
        return generateToken(dto, REFRESH_TOKEN);
    }

    private String generateToken(CreateTokenDto dto, String tokenType) {
        return Jwts.builder()
            .setSubject(dto.userId().toString())
            .claim(EMAIL, dto.email())
            .claim(TOKEN_TYPE, tokenType)
            .setIssuedAt(dto.issuedAt())
            .setExpiration(dto.expiration())
            .signWith(signingKey, HS256)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    private void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
    }

    public boolean isValid(String token) {
        try {
            validateToken(token); // 위의 메소드 이용
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public String getEmail(String token) {
        Claims claims = parseToken(token);
        return claims.get(EMAIL).toString();
    }
}
