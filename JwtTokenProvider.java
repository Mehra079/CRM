package com.example.authentication.service;

import com.example.authentication.dto.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {
    JwtResponse createToken(String username);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String extractTokenFromRequest(HttpServletRequest request);
}
