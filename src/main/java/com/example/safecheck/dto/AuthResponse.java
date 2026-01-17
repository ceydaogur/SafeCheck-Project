package com.example.safecheck.dto;

public class AuthResponse {
    public Long userId;
    public String token;
    public String name;
    public String email;

    public AuthResponse(Long userId, String token, String name, String email) {
        this.userId = userId;
        this.token = token;
        this.name = name;
        this.email = email;
    }
}
