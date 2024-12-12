package com.example.authentication.service;

import com.example.authentication.dto.JwtResponse;
import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.PasswordChangeRequest;
import com.example.authentication.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    void verifyOtp(String username, String otp);
    void logout(HttpServletRequest request);
    void resetPassword(PasswordResetRequest request);
    void changePassword(HttpServletRequest request, PasswordChangeRequest passwordChangeRequest);
}
