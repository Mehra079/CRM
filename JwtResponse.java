package com.example.authentication.dto;

public class JwtResponse {

    private String token;
    private String username;

    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
    public JwtResponse(String token) {
        this.token = token;
    }


    // Getters and Setters or Lombok annotations

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
