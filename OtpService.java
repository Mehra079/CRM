package com.example.authentication.service;

public interface OtpService {
    void sendOtp(String mobile);
    boolean verifyOtp(String mobile, String otp);
    void clearOtp(String mobile);
}
