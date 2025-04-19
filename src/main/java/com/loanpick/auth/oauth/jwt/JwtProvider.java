package com.loanpick.auth.oauth.jwt;

import com.loanpick.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final String secretKey = "thisjwtkeyisloanpicksjwtsecretkeybykosanseong";

    public String createToken(User user, Date now, Date expiration) {

        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("email", user.getEmail())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
