package com.frankit.assignment.api.service.auth;


import com.frankit.assignment.api.service.auth.response.AuthResponse;
import com.frankit.assignment.domain.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-sec}") long accessTokenExpirationSec,
            @Value("${jwt.refresh-token-expiration-sec}") long refreshTokenExpirationSec
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationSec * 1000;
        this.refreshTokenExpirationMs = refreshTokenExpirationSec * 1000;
    }

    public AuthResponse generateToken(User user) {
        String accessToken = createToken(user.getId(), accessTokenExpirationMs);
        String refreshToken = createToken(user.getId(), refreshTokenExpirationMs);
        return AuthResponse.of(accessToken, refreshToken);
    }

    private String createToken(Long userId, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        return Long.parseLong(parseToken(token).getBody().getSubject());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

}

