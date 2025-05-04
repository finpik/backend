package com.loanpick.auth.oauth.jwt;

import static com.loanpick.util.Values.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loanpick.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    private final String secretKey;

    public JwtProvider(@Value("${fin-pick.jwt.secretKey}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + FIFTEEN_MINUTE_MILL);
        return createAccessToken(user, now, expiry);
    }

    public String createAccessToken(User user, Date issuedAt, Date expiration) {
        return Jwts.builder().setSubject(user.getId().toString()).claim(EMAIL, user.getEmail()).setIssuedAt(issuedAt)
                .setExpiration(expiration).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + FOURTEEN_DAYS_MILL);
        return createRefreshToken(user, now, expiry);
    }

    public String createRefreshToken(User user, Date issuedAt, Date expiration) {
        return Jwts.builder().setSubject(user.getId().toString()).claim(EMAIL, user.getEmail()).setIssuedAt(issuedAt)
                .setExpiration(expiration).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
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
}
