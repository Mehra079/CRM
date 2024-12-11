package com.project.Bootcamp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {
    @NotBlank
    @Email
    private String email;

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }
}
