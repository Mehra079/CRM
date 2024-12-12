package com.example.authentication.controller;

import com.example.authentication.dto.*;
import com.example.authentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String username, @RequestParam String otp) {
        authService.verifyOtp(username, otp);
        return ResponseEntity.ok("OTP verified successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("User logged out successfully.");
    }
    // Verify OTP and reset password
    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return new ApiResponse(true, "Password reset successful.");
    }

    // Change password for authenticated users
    @PostMapping("/change-password")
    public ApiResponse changePassword(HttpServletRequest request, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.changePassword(request,passwordChangeRequest);
        return new ApiResponse(true, "Password changed successfully.");
    }

}
