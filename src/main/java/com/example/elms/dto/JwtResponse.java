package com.example.elms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class JwtResponse {
    
    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";
    
    // Constructor
    public JwtResponse(String token) {
        this.token = token;
    }
    
    // Getters and setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}