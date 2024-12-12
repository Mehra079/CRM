package com.example.authentication.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;

public class JwtUtils {

    private static final Key key = Keys.hmacShaKeyFor("your_secret_key".getBytes(StandardCharsets.UTF_8));

    public static Instant getTokenExpiry(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .toInstant();
    }
}
