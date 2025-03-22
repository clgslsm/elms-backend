package com.example.elms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class SignupRequest {
    
    @Schema(description = "User's username", example = "john.doe", required = true)
    private String username;
    
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
    
    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

}