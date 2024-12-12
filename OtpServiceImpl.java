package com.example.authentication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    // Store OTP along with its expiration time
    private final Map<String, OtpDetails> otpStore = new ConcurrentHashMap<>();

    private final SmsService smsService;

    @Value("${otp.expiry.duration:5}") // OTP expiration time in minutes
    private int otpExpiryDurationInMinutes;

    public OtpServiceImpl(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void sendOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Generate a 6-digit OTP
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(otpExpiryDurationInMinutes);

        otpStore.put(mobile, new OtpDetails(otp, expiryTime));

        // Simulate sending OTP via SMS through SmsService
        smsService.sendSms(mobile, "Your OTP is: " + otp);

        logger.info("OTP sent to {}: {}", mobile, otp);
    }

    @Override
    public boolean verifyOtp(String mobile, String otp) {
        OtpDetails otpDetails = otpStore.get(mobile);

        if (otpDetails == null) {
            return false;
        }

        if (otpDetails.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(mobile); // Clean expired OTP
            logger.warn("OTP expired for {}", mobile);
            return false;
        }

        boolean isValid = otpDetails.getOtp().equals(otp);
        if (isValid) {
            otpStore.remove(mobile); // OTP is verified, remove from store
        }

        return isValid;
    }

    @Override
    public void clearOtp(String mobile) {
        otpStore.remove(mobile);
    }

    private static class OtpDetails {
        private final String otp;
        private final LocalDateTime expirationTime;

        public OtpDetails(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
