package org.praveenit.bookfloww.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.praveenit.bookfloww.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access.expiration}")
	private long expiration;
	
	private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	public String generateAccessToken(User user) {

	    return Jwts.builder()
	        .setSubject(user.getEmail())
	        .claim("userId", user.getId())
	        .claim("role", user.getRole().name())
	        .claim("loginType", "GOOGLE")
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis() + expiration))
	        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	        .compact();
	}
	//✅ SINGLE validate method (no duplicates)
	public Claims validateAndGetClaims(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey())
	            .build()
	            .parseClaimsJws(token)   // validates + throws exceptions
	            .getBody();              // returns claims if valid
	}
}
