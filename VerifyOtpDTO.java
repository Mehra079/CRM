package com.project.Bootcamp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String otp;

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getOtp() {
        return otp;
    }

    public void setOtp(@NotBlank String otp) {
        this.otp = otp;
    }
}
