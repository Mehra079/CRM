package com.example.authentication.service;

import com.example.authentication.dto.JwtResponse;
import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.PasswordChangeRequest;
import com.example.authentication.dto.PasswordResetRequest;
import com.example.authentication.entity.User;
import com.example.authentication.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                           OtpService otpService, PasswordEncoder passwordEncoder,
                           TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        if (user.getFailedLoginAttempts() >= 3) {
            throw new IllegalStateException("Account locked. Contact support.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            incrementFailedLoginAttempts(user);
            throw new IllegalArgumentException("Invalid password");
        }

        resetFailedLoginAttempts(user);
        otpService.sendOtp(user.getMobile());

        return jwtTokenProvider.createToken(user.getUsername());
    }

    @Override
    public void verifyOtp(String username, String otp) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        if (!otpService.verifyOtp(user.getMobile(), otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtTokenProvider.extractTokenFromRequest(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        tokenBlacklistService.addToBlacklist(token);
    }
    @Override
    public void changePassword(HttpServletRequest request, PasswordChangeRequest passwordChangeRequest) {
        String token = jwtTokenProvider.extractTokenFromRequest(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException("User not authenticated.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match.");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByMobile(request.getMobile())
                .orElseThrow(() -> new IllegalArgumentException("Mobile number not found"));

        if (!otpService.verifyOtp(user.getMobile(), request.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
    private void incrementFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        userRepository.save(user);
    }

    private void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }
}
