package com.example.elms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest {
    
    @Schema(description = "User's username or email", example = "john.doe", required = true)
    private String username;
    
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
}