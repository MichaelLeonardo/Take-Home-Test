package com.example.takehometest.auth;

import com.example.takehometest.dto.response.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtil {

    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, LoginResponse loginResponse) {
        return Jwts.builder()
                .setSubject(username)
                .claim("is_admin", loginResponse.getIsAdmin())
                .claim("permissions", loginResponse.getListMenuUrl())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // ✅ pakai key yang sama
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validate(String token) {
        try {
            extractAllClaims(token); // ✅ reuse method
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

