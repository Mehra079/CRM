package com.project.Bootcamp.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String captchaToken;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordMatching() {
        return password.equals(confirmPassword);
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @NotBlank String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(@NotBlank String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public @NotBlank String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(@NotBlank String captchaToken) {
        this.captchaToken = captchaToken;
    }
}
