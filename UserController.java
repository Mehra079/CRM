package com.project.Bootcamp.controller;

import com.project.Bootcamp.dto.*;
import com.project.Bootcamp.service.AuthenticationService;
import com.project.Bootcamp.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public UserController(UserRegistrationService registrationService, AuthenticationService authenticationService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserDTO dto) {
        registrationService.registerUser(dto);
        return ResponseEntity.ok("Registration successful. Please verify your email.");
    }

    // Verify email address
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        registrationService.verifyEmail(email, code);
        return ResponseEntity.ok("Email verification successful.");
    }

    // Login user with 2FA
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequestDTO dto) {
        String message = authenticationService.login(dto);
        return ResponseEntity.ok(message);
    }

    // Verify OTP for 2FA
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO dto) {
        String token = authenticationService.verifyOtp(dto);
        return ResponseEntity.ok("Login successful. JWT Token: " + token);
    }

    // Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        authenticationService.sendPasswordResetLink(dto.getEmail());
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    // Change password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        authenticationService.changePassword(dto);
        return ResponseEntity.ok("Password changed successfully.");
    }
}
