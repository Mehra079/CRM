package com.project.Bootcamp.service;

import com.project.Bootcamp.dto.ChangePasswordDTO;
import com.project.Bootcamp.dto.LoginRequestDTO;
import com.project.Bootcamp.dto.VerifyOtpDTO;
import com.project.Bootcamp.entity.User;
import com.project.Bootcamp.exception.UserNotFoundException;
import com.project.Bootcamp.repository.UserRepository;
import com.project.Bootcamp.util.JwtTokenUtil;
import com.project.Bootcamp.util.OtpSender;
import com.project.Bootcamp.util.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Value("${password.reset.link}")
    private String passwordResetLink;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final OtpSender otpSender;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    private final Map<String, String> otpStore = new HashMap<>();

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, OtpSender otpSender, PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.otpSender = otpSender;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    public String login(LoginRequestDTO dto) {
        try {
            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

            // Generate OTP and send it
            String otp = otpSender.generateOtp();
            otpStore.put(dto.getUsername(), otp);

            User user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            otpSender.sendOtp(user.getMobileNumber(), otp);

            return "OTP sent to your registered mobile number.";

        } catch (BadCredentialsException ex) {
            incrementFailedAttempts(dto.getUsername());
            throw ex;
        }
    }

    public String verifyOtp(VerifyOtpDTO dto) {
        if (otpStore.containsKey(dto.getUsername()) && otpStore.get(dto.getUsername()).equals(dto.getOtp())) {
            otpStore.remove(dto.getUsername());

            User user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            resetFailedAttempts(user);

            return jwtTokenUtil.generateToken(user);
        } else {
            throw new IllegalArgumentException("Invalid OTP.");
        }
    }

    private void incrementFailedAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        if (user.getFailedLoginAttempts() >= 3) {
            user.setLocked(true);
        }
        userRepository.save(user);
    }

    private void resetFailedAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setLocked(false);
        userRepository.save(user);
    }

    public void sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String subject = "Password Reset Request";
        String body = "Click this link to reset your password: " + passwordResetLink;

        emailSender.sendEmail(user.getEmail(), subject, body);
    }

    public void changePassword(ChangePasswordDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Current password is incorrect.");
        }
    }
}
