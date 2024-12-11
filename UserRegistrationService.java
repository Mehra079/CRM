package com.project.Bootcamp.service;

import com.project.Bootcamp.dto.RegisterUserDTO;
import com.project.Bootcamp.entity.User;
import com.project.Bootcamp.exception.UserAlreadyExistsException;
import com.project.Bootcamp.repository.UserRepository;
import com.project.Bootcamp.util.CaptchaVerifier;
import com.project.Bootcamp.util.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final CaptchaVerifier captchaVerifier;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailSender emailSender, CaptchaVerifier captchaVerifier) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.captchaVerifier = captchaVerifier;
    }

    public void registerUser(RegisterUserDTO dto) {
        // Captcha verification
        if (!captchaVerifier.verify(dto.getCaptchaToken())) {
            throw new IllegalArgumentException("Invalid CAPTCHA");
        }

        // Check if the user already exists
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email already exists.");
        }

        // Create and save the user
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setMobileNumber(dto.getMobileNumber());
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setVerificationExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send confirmation email
        String verificationLink = "http://localhost:8080/api/v1/users/verify?email=" + dto.getEmail() + "&code=" + user.getVerificationCode();
        emailSender.sendEmail(dto.getEmail(), "Verify your account", "Click here to verify: " + verificationLink);
    }

    public void verifyEmail(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));
        if (user.getVerificationCode().equals(code) && LocalDateTime.now().isBefore(user.getVerificationExpiry())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationExpiry(null);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid or expired verification code.");
        }
    }
}
