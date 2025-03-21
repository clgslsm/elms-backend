package com.example.elms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SignupRequest {
    
    @Schema(description = "User's username", example = "john.doe", required = true)
    private String username;
    
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
    
    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}