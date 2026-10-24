package com.gateway.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        Object role = extractAllClaims(token).get("role");
        return role == null ? "USER" : role.toString();
    }

    public boolean isWeCoreSessionToken(String token) {
        return token != null && (token.startsWith("$2a$") || token.startsWith("$2b$") || token.startsWith("$2y$"));
    }

    public boolean isValid(String token) {
        if (isWeCoreSessionToken(token)) {
            return true;
        }
        try {
            return !extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
