package com.example.authentication.service;

import com.example.authentication.util.JwtUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    @Override
    public void addToBlacklist(String token) {
        Instant expiry = JwtUtils.getTokenExpiry(token); // Extract token expiry
        blacklist.put(token, expiry);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        Instant expiry = blacklist.get(token);
        if (expiry == null) {
            return false;
        }
        if (expiry.isBefore(Instant.now())) {
            blacklist.remove(token); // Clean up expired tokens
            return false;
        }
        return true;
    }
}
