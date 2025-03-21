package com.example.elms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {
    
    @Schema(description = "User's username or email", example = "john.doe", required = true)
    private String username;
    
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}