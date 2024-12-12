package com.example.authentication.service;

public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isTokenBlacklisted(String token);
}
