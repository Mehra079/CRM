package com.example.authentication.dto;

public class PasswordResetRequest {
    private String mobile;
    private String otp;
    private String newPassword;

    // Getters and Setters
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
