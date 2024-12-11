package com.project.Bootcamp.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpSender {

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendOtp(String mobileNumber, String otp) {
        // Simulate sending OTP
        System.out.println("OTP sent to " + mobileNumber + ": " + otp);
    }
}
