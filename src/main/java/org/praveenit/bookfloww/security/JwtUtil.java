package org.praveenit.bookfloww.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private static final String LOGIN_TYPE_GOOGLE = "GOOGLE";
    private static final String ISSUER = "bookfloww-api";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long expiration;

    private Key signingKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("loginType", LOGIN_TYPE_GOOGLE)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates token and returns claims.
     * Throws JwtException if token is invalid or expired.
     */
    public Claims validateAndGetClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            log.error("Invalid JWT token", ex);
            throw ex;
        }
    }
}
