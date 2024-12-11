package com.project.Bootcamp.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChangePasswordDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordMatching() {
        return newPassword.equals(confirmPassword);
    }

    public @NotBlank String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(@NotBlank String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public @NotBlank String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank String newPassword) {
        this.newPassword = newPassword;
    }

    public @NotBlank String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }
}
